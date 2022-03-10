package sni.common.services.implementations;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sni.common.models.dtos.FileDTO;
import sni.common.services.FilePersistenceService;

@Service
public class FilePersistenceServiceImpl implements FilePersistenceService
{
    @Override
    public void persistFile(int fileID, short fileVersion, Resource file)
    {

    }

    @Override
    public Resource getFile(int fileID, short fileVersion)
    {
        return null;
    }
}
