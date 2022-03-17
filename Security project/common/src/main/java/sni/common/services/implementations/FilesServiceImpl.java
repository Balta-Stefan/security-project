package sni.common.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sni.common.exceptions.BadRequestException;
import sni.common.exceptions.ForbiddenException;
import sni.common.exceptions.InternalServerError;
import sni.common.exceptions.NotFoundException;
import sni.common.models.dtos.*;
import sni.common.models.entities.*;
import sni.common.models.enums.Operation;
import sni.common.models.enums.Role;
import sni.common.repositories.FileVersionsRepository;
import sni.common.repositories.FilesRepository;
import sni.common.repositories.LogsRepository;
import sni.common.repositories.UsersRepository;
import sni.common.services.FilePersistenceService;
import sni.common.services.FilesService;
import sni.common.services.NotificationService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
public class FilesServiceImpl implements FilesService
{
    private final FilesRepository filesRepository;
    private final UsersRepository usersRepository;
    private final LogsRepository logsRepository;
    private final FileVersionsRepository fileVersionsRepository;
    private final FilePersistenceService filePersistenceService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public FilesServiceImpl(FilesRepository filesRepository,
                            UsersRepository usersRepository,
                            LogsRepository logsRepository,
                            FileVersionsRepository fileVersionsRepository, FilePersistenceService filePersistenceService,
                            NotificationService notificationService, ModelMapper modelMapper)
    {
        this.filesRepository = filesRepository;
        this.usersRepository = usersRepository;
        this.logsRepository = logsRepository;
        this.fileVersionsRepository = fileVersionsRepository;
        this.filePersistenceService = filePersistenceService;
        this.notificationService = notificationService;
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

    private boolean isFileAChild(FileEntity rootDir, FileEntity child)
    {
        while( child != null && (child.getFileId() != rootDir.getFileId()) )
        {
            child = child.getParent();
        }

        return child != null;
    }

    private void checkFileBelongsToUserRoot(UserEntity user, FileEntity file)
    {
        FileEntity root = user.getRootDir();
        if(this.isFileAChild(root, file) == false)
        {
            throw new ForbiddenException();
        }
    }

    private void sanitizeString(String str, UserEntity user, Operation operation)
    {
        Pattern pattern = Pattern.compile(".*[<>/\\\\;].*");
        Matcher matcher = pattern.matcher(str);

        if(matcher.matches())
        {
            log.warn("User: " + user.getUserId() + " has given a string with forbidden character.Operation: " + operation.name() + ", string is: " + str);
            throw new BadRequestException();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('DIR_ADMIN')")
    public void moveFile(int fileID, int newParentID, int askerID)
    {
        if(fileID == newParentID)
        {
            throw new ForbiddenException();
        }

        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);
        FileEntity fileToMove = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        FileEntity newParent = filesRepository.findById(newParentID).orElseThrow(NotFoundException::new);
        if(fileToMove.getDiscarded() || fileToMove.getDeleted())
        {
            throw new NotFoundException();
        }
        if(newParent.getDiscarded() || newParent.getDeleted())
        {
            throw new NotFoundException();
        }
        if(newParent.getIsDirectory() == false)
        {
            // files can only be moved into directories, not other files
            throw new ForbiddenException();
        }

        // moving a file to its child is not possible
        if(this.isFileAChild(fileToMove, newParent) == true)
        {
            throw new ForbiddenException();
        }


        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToMove);
        this.checkFileBelongsToUserRoot(user, newParent);


        String descPart = (fileToMove.getIsDirectory()) ? "directory" : "file";
        String description = "Moving " + descPart + "(" + fileToMove.getFileId() + ")" + fileToMove.getName() + " to (" + newParent.getFileId() + ")" + newParent.getName();

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.MOVE, fileToMove, user, description);

        fileToMove.setParent(newParent);
        fileToMove = filesRepository.saveAndFlush(fileToMove);
    }

    @Override
    @PreAuthorize("hasAuthority('DIR_ADMIN') or (hasAuthority('USER') and hasAuthority('UPDATE'))")
    public void renameFile(int fileID, int askerID, String newName)
    {
        FileEntity fileToRename = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        if(fileToRename.getDiscarded() || fileToRename.getDeleted())
        {
            throw new NotFoundException();
        }

        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);
        Operation op = (fileToRename.getIsDirectory()) ? Operation.RENAME_DIR : Operation.RENAME_FILE;
        this.sanitizeString(newName, user, op);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToRename);

        String descPart = (fileToRename.getIsDirectory()) ? "directory" : "file";

        String description = "Renaming " + descPart + "(" + fileToRename.getFileId() + ")" + fileToRename.getName() + " to " + newName;

        FileLogEntity fileLogEntity = this.logUtilMethod(op, fileToRename, user, description);

        fileToRename.setName(newName);
        fileToRename = filesRepository.saveAndFlush(fileToRename);
    }

    @Override
    public DirectoryDTO listDir(int fileID, int askerID)
    {
        FileEntity fileToRead = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // only ADMIN can see discarded or deleted files
        if((fileToRead.getDiscarded() || fileToRead.getDeleted()) && user.getRole().equals(Role.ADMIN) == false)
        {
            throw new NotFoundException();
        }

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToRead);

        if(fileToRead.getIsDirectory() == false)
        {
            throw new NotFoundException();
        }

        DirectoryDTO directoryDTO = modelMapper.map(fileToRead, DirectoryDTO.class);
        // EXCLUDE DISCARDED OR DELETED CHILDREN IF USER ISN'T ADMIN!
        if(user.getRole().equals(Role.ADMIN) == false)
        {
            List<FileBasicDTO> children = directoryDTO.getChildren();
            children = children.stream()
                    .filter(child -> child.getDiscarded() == false && child.getDeleted() == false)
                    .toList();
            directoryDTO.setChildren(children);
        }

        List<DirectoryBasicDTO> breadCrumbs = new ArrayList<>();
        FileEntity parent = fileToRead;

        FileEntity userRoot = user.getRootDir();
        if(fileToRead.getFileId() == userRoot.getFileId())
        {
            return directoryDTO;
        }

        while((parent = parent.getParent()) != null)
        {
            breadCrumbs.add(modelMapper.map(parent, DirectoryBasicDTO.class));
            if(parent.getFileId() == userRoot.getFileId())
                break;
        }
        Collections.reverse(breadCrumbs);
        directoryDTO.setBreadCrumbs(breadCrumbs);

        return directoryDTO;
    }

    private FileEntity createNewFile(int parentID, String name, boolean isDir, int creatorID)
    {
        FileEntity newFileParent = filesRepository.findById(parentID).orElseThrow(NotFoundException::new);
        if(newFileParent.getDiscarded() || newFileParent.getDeleted())
        {
            throw new NotFoundException();
        }

        UserEntity user = usersRepository.findById(creatorID).orElseThrow(NotFoundException::new);
        Operation op = (isDir == true) ? Operation.CREATE_DIR : Operation.CREATE_FILE;
        this.sanitizeString(name, user, op);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, newFileParent);

        FileLogEntity fileLogEntity = this.logUtilMethod(op, newFileParent, user, "");

        FileEntity newFile = new FileEntity();
        newFile.setName(name);

        newFile.setDiscarded(false);
        newFile.setDeleted(false);
        newFile.setNumOfVersions((short) 0);
        newFile.setParent(newFileParent);
        newFile.setIsDirectory(isDir);

        newFile = filesRepository.saveAndFlush(newFile);

        return newFile;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIR_ADMIN')")
    public FileDTO createDir(int parentID, String name, int creatorID)
    {
        FileEntity file = this.createNewFile(parentID, name, true, creatorID);
        return modelMapper.map(file, FileDTO.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FileLogDTO> getLogs(int fileID)
    {
        FileEntity fileEntity = this.filesRepository.findById(fileID).orElseThrow(NotFoundException::new);

        List<FileLogEntity> logs = this.logsRepository.findAllByAffectedFile(fileEntity);

        return logs
                .stream()
                .map(l -> modelMapper.map(l, FileLogDTO.class))
                .toList();
    }

    @Override
    public DirectoryDTO getRoot(int userID)
    {
        UserEntity userEntity = this.usersRepository.findById(userID).orElseThrow(NotFoundException::new);
        FileEntity root = userEntity.getRootDir();

        if(root == null)
        {
            return new DirectoryDTO(null, null, Collections.emptyList());
        }

        return this.listDir(root.getFileId(), userID);
    }

    /*@Override
    public List<DirectoryDTO> getBreadCrumbs(int directoryID, int askerID)
    {
        UserEntity userEntity = this.usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        FileEntity fileToRead = filesRepository.findById(directoryID).orElseThrow(NotFoundException::new);
        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(userEntity, fileToRead);

        if(fileToRead.getDiscarded() || fileToRead.getDeleted())
        {
            throw new NotFoundException();
        }

        FileEntity userRoot = userEntity.getRootDir();
        FileEntity parent = fileToRead.getParent();

        List<DirectoryDTO> breadcrumbs = new ArrayList<>();

        while((parent = parent.getParent()) != null)
        {
            breadcrumbs.add(modelMapper.map(parent, DirectoryDTO.class));
            if(parent.getFileId() == userRoot.getFileId())
                break;
        }

        Collections.reverse(breadcrumbs);

        return breadcrumbs;
    }*/

    @Override
    @PreAuthorize("hasAuthority('DIR_ADMIN') or (hasAuthority('USER') and hasAuthority('CREATE'))")
    public FileBasicDTO createFile(int parentID, Resource fileData, int creatorID)
    {
        FileEntity file = this.createNewFile(parentID, fileData.getFilename(), false, creatorID);
        UserEntity creator = this.usersRepository.findById(creatorID).orElseThrow(NotFoundException::new);

        this.sanitizeString(fileData.getFilename(), creator, Operation.CREATE_FILE);

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.CREATE_FILE, file, creator, null);
        try
        {
            filePersistenceService.persistFile(file.getFileId(), (short) 0, fileData);
        }
        catch(Exception e)
        {
            throw new InternalServerError();
        }

        return modelMapper.map(file, FileBasicDTO.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIR_ADMIN') or (hasAuthority('USER') and hasAuthority('READ'))")
    public FileResourceDownloadWrapper readFile(int fileID, Optional<Short> requestedVersion, int askerID)
    {
        FileEntity fileToRead = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        if(fileToRead.getDiscarded() || fileToRead.getDeleted())
        {
            throw new NotFoundException();
        }
        if(fileToRead.getIsDirectory() == true)
        {
            throw new NotFoundException();
        }

        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToRead);

        FileResourceDownloadWrapper fileWrapper = new FileResourceDownloadWrapper();
        fileWrapper.setFileName(fileToRead.getName());

        Resource fileData;
        if(requestedVersion.isPresent())
        {
            // get specified version
            fileData = filePersistenceService.getFile(fileID, requestedVersion.get());
        }
        else
        {
            // get latest version
            fileData = filePersistenceService.getFile(fileID, fileToRead.getNumOfVersions());
        }

        fileWrapper.setFile(fileData);
        return fileWrapper;
    }

    @Override
    @PreAuthorize("hasAuthority('DIR_ADMIN') or (hasAuthority('USER') and hasAuthority('UPDATE'))")
    public FileDTO updateFile(int fileID, Resource fileData, int askerID)
    {
        // creates a new version

        FileEntity fileToUpdate = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        if(fileToUpdate.getDiscarded() || fileToUpdate.getDeleted())
        {
            throw new NotFoundException();
        }

        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);
        this.sanitizeString(fileData.getFilename(), user, Operation.UPDATE_FILE);

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToUpdate);

        if(fileToUpdate.getIsDirectory() == true)
        {
            throw new ForbiddenException();
        }

        FileVersionEntity fileVersionEntity = new FileVersionEntity();
        fileVersionEntity.setFirstVersion(fileToUpdate);
        fileVersionEntity.setFileVersionEntityPK(new FileVersionEntityPK(fileToUpdate.getFileId(), (short) (fileToUpdate.getNumOfVersions()+1)));

        fileVersionEntity = fileVersionsRepository.saveAndFlush(fileVersionEntity);

        FileLogEntity fileLogEntity = this.logUtilMethod(Operation.UPDATE_FILE, fileToUpdate, user, null);

        entityManager.refresh(fileToUpdate);

        return modelMapper.map(fileToUpdate, FileDTO.class);
    }

    @Override
    @PreAuthorize("hasAuthority('DIR_ADMIN') or (hasAuthority('USER') and hasAuthority('DELETE'))")
    public void deleteFile(int fileID, int askerID)
    {
        FileEntity fileToDelete = filesRepository.findById(fileID).orElseThrow(NotFoundException::new);
        UserEntity user = usersRepository.findById(askerID).orElseThrow(NotFoundException::new);

        if(fileToDelete.getIsDirectory() == true && user.getRole().equals(Role.USER))
        {
            throw new ForbiddenException();
        }

        // check whether the user is authorised to perform this operation
        this.checkFileBelongsToUserRoot(user, fileToDelete);

        //String description = "Deleting a " + ((fileToDelete.getIsDirectory() == true) ? "directory" : "file");
        Operation op = (fileToDelete.getIsDirectory() == true) ? Operation.DELETE_DIRECTORY : Operation.DELETE_FILE;
        FileLogEntity fileLogEntity = this.logUtilMethod(op, fileToDelete, user, null);


        fileToDelete.setDiscarded(true);
        fileToDelete = filesRepository.saveAndFlush(fileToDelete);

        // if the user is deleting a directory, set all its contents to discarded
        List<FileEntity> currentLevelChildren = fileToDelete.getChildren();

        this.notificationService.notifyOfAction(fileLogEntity);

        while(currentLevelChildren.size() != 0)
        {
            List<FileEntity> temp = new ArrayList<>();
            for(FileEntity fe : currentLevelChildren)
            {
                fe.setDiscarded(true);
                filesRepository.saveAndFlush(fe);
                Operation tmpOp = (fe.getIsDirectory() == true) ? Operation.DELETE_DIRECTORY : Operation.DELETE_FILE;
                FileLogEntity tmpLog = this.logUtilMethod(op, fe, user, "");

                this.notificationService.notifyOfAction(tmpLog);

                temp.addAll(fe.getChildren());
            }

            currentLevelChildren = temp;
        }
    }

}
