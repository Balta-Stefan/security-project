package sni.common.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sni.common.exceptions.BadRequestException;
import sni.common.exceptions.ForbiddenException;
import sni.common.exceptions.NotFoundException;
import sni.common.models.dtos.DirectoryDTO;
import sni.common.models.dtos.FileDTO;
import sni.common.models.entities.*;
import sni.common.models.enums.Operation;
import sni.common.models.enums.Role;
import sni.common.repositories.FileVersionsRepository;
import sni.common.repositories.FilesRepository;
import sni.common.repositories.LogsRepository;
import sni.common.repositories.UsersRepository;
import sni.common.services.FilePersistenceService;
import sni.common.services.FilesService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FilesServiceImpl implements FilesService
{
    private final FilesRepository filesRepository;
    private final UsersRepository usersRepository;
    private final LogsRepository logsRepository;
    private final FileVersionsRepository fileVersionsRepository;
    private final FilePersistenceService filePersistenceService;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Integer rootID = 0;

    public FilesServiceImpl(FilesRepository filesRepository,
                            UsersRepository usersRepository,
                            LogsRepository logsRepository,
                            FileVersionsRepository fileVersionsRepository, FilePersistenceService filePersistenceService,
                            ModelMapper modelMapper)
    {
        this.filesRepository = filesRepository;
        this.usersRepository = usersRepository;
        this.logsRepository = logsRepository;
        this.fileVersionsRepository = fileVersionsRepository;
        this.filePersistenceService = filePersistenceService;
        this.modelMapper = modelMapper;
    }

    private FileLogEntity logUtilMethod(Operation operation, FileEntity affectedFile, UserEntity user, String description)
    {
        FileLogEntity fileLogEntity = new FileLogEntity();

        fileLogEntity.setTimestamp(LocalDateTime.now());
        fileLogEntity.setOperation(operation);
        fileLogEntity.setAffectedFile(affectedFile);
        fileLogEntity.setCreatedBy(user);
        fileLogEntity.setDescription(description);

        fileLogEntity = logsRepository.saveAndFlush(fileLogEntity);

        return fileLogEntity;
    }

    private void checkFileBelongsToRoot(UserEntity user, FileEntity file)
    {
        FileEntity root = user.getRootDir();

        FileEntity tempFile = file;
        while(tempFile != null && tempFile.getFileId() != root.getFileId())
        {
            tempFile = tempFile.getParent();
        }

        if(tempFile != null && tempFile.getFileId() == rootID)
        {
            throw new ForbiddenException();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN')")
    public FileDTO moveFile(int fileID, int newParentID, int askerID)
    {
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);
        FileEntity fileToMove = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        FileEntity newParent = filesRepository.findById(newParentID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToMove);
        this.checkFileBelongsToRoot(user, newParent);


        String descPart = (fileToMove.getIsDirectory()) ? "directory" : "file";
        String description = "Moving " + descPart + " to " + newParent.getName();

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.MOVE, fileToMove, user, description);

        fileToMove.setParent(newParent);
        fileToMove = filesRepository.saveAndFlush(fileToMove);

        return modelMapper.map(fileToMove, FileDTO.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN', 'USER')")
    public FileDTO renameFile(int fileID, int askerID, String newName)
    {
        FileEntity fileToRename = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToRename);

        String descPart = (fileToRename.getIsDirectory()) ? "directory" : "file";
        Operation op = (fileToRename.getIsDirectory()) ? Operation.RENAME_DIR : Operation.RENAME_FILE;
        String description = "Renaming " + descPart + " to " + newName;

        FileLogEntity fileLogEntity = this.logUtilMethod(op, fileToRename, user, description);

        fileToRename.setName(newName);
        fileToRename = filesRepository.saveAndFlush(fileToRename);

        return modelMapper.map(fileToRename, FileDTO.class);
    }

    @Override
    public List<DirectoryDTO> listDir(int fileID, int askerID)
    {
        FileEntity fileToRead = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToRead);

        if(fileToRead.getIsDirectory() == false)
        {
            throw new NotFoundException();
        }

        return fileToRead
                .getChildren()
                .stream()
                .map(c -> modelMapper.map(c, DirectoryDTO.class))
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN')")
    public FileDTO createDir(FileDTO toCreate, int creatorID)
    {
        FileEntity newFileParent = filesRepository.findById(toCreate.getParent()).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(creatorID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, newFileParent);

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.CREATE_DIR, newFileParent, user, "");

        FileEntity newFile = new FileEntity();
        newFile.setName(toCreate.getName());

        newFile.setDiscarded(false);
        newFile.setDeleted(false);
        newFile.setNumOfVersions((short) 0);
        newFile.setParent(newFileParent);
        newFile.setIsDirectory(true);

        newFile = filesRepository.saveAndFlush(newFile);

        return modelMapper.map(newFile, FileDTO.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN', 'USER')")
    public FileDTO createFile(@Valid FileDTO toCreate, Resource fileData, int creatorID)
    {
        FileEntity newFileParent = filesRepository.findById(toCreate.getParent()).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(creatorID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, newFileParent);

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.CREATE_FILE, newFileParent, user, "");

        FileEntity newFile = new FileEntity();
        newFile.setName(toCreate.getName());

        newFile.setDiscarded(false);
        newFile.setDeleted(false);
        newFile.setNumOfVersions((short) 0);
        newFile.setParent(newFileParent);
        newFile.setIsDirectory(false);

        newFile = filesRepository.saveAndFlush(newFile);

        filePersistenceService.persistFile(newFile.getFileId(), (short)0, fileData);

        return modelMapper.map(newFile, FileDTO.class);
    }

    @Override
    public Resource readFile(int fileID, short requestedVersion, int askerID)
    {
        FileEntity fileToRead = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToRead);

        if(fileToRead.getIsDirectory() == true)
        {
            throw new NotFoundException();
        }

        return filePersistenceService.getFile(fileID, requestedVersion);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN', 'USER')")
    public FileDTO updateFile(int fileID, Resource fileData, int askerID)
    {
        // creates a new version

        FileEntity fileToUpdate = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToUpdate);

        if(fileToUpdate.getIsDirectory() == true)
        {
            throw new NotFoundException();
        }

        FileVersionEntity fileVersionEntity = new FileVersionEntity();
        fileVersionEntity.setFirstVersion(fileToUpdate);
        fileVersionEntity.setFileVersionEntityPK(new FileVersionEntityPK(fileToUpdate.getFileId(), (short) (fileToUpdate.getNumOfVersions()+1)));

        fileVersionEntity = fileVersionsRepository.saveAndFlush(fileVersionEntity);

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.UPDATE_FILE, fileToUpdate.getParent(), user, "");

        entityManager.refresh(fileToUpdate);

        return modelMapper.map(fileToUpdate, FileDTO.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN', 'USER')")
    public void deleteFile(int fileID, int askerID)
    {
        FileEntity fileToDelete = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        if(fileToDelete.getIsDirectory() == true && user.getRole().equals(Role.USER))
        {
            throw new ForbiddenException();
        }

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToRoot(user, fileToDelete);

        String description = "Deleting a " + ((fileToDelete.getIsDirectory() == true) ? "directory" : "file");
        Operation op = (fileToDelete.getIsDirectory() == true) ? Operation.DELETE_DIRECTORY : Operation.DELETE_FILE;
        FileLogEntity fileLogEntity = this.logUtilMethod(op, fileToDelete, user, description);


        fileToDelete.setDiscarded(true);
        fileToDelete = filesRepository.saveAndFlush(fileToDelete);
    }
}
