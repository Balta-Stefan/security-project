package sni.common.services.implementations;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sni.common.models.dtos.FileDTO;
import sni.common.repositories.FilesRepository;
import sni.common.services.FilesService;

import javax.transaction.Transactional;

@Service
@Transactional
public class FilesServiceImpl implements FilesService
{
    private final FilesRepository filesRepository;

    public FilesServiceImpl(FilesRepository filesRepository)
    {
        this.filesRepository = filesRepository;
    }


    @Override
    public FileDTO createDirectory(int parentID, int creatorID)
    {
        return null;
    }

    @Override
    public FileDTO createFile(int parentID, int creatorID, Resource file)
    {
        return null;
    }

    @Override
    public FileDTO moveFile(int fileID, int newParentID, int applicantUserID)
    {
        return null;
    }

    @Override
    public void deleteFile(int fileID, int applicantUserID)
    {

    }
}
