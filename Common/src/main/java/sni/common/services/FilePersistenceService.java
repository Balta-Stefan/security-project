package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.FileDTO;

public interface FilePersistenceService
{
    void persistFile(int fileID, short fileVersion, Resource file);
    Resource getFile(int fileID, short fileVersion);
}
