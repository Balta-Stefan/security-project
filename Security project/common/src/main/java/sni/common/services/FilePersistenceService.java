package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.FileDTO;

import java.io.IOException;

public interface FilePersistenceService
{
    void persistFile(int fileID, short fileVersion, Resource file) throws IOException;
    Resource getFile(int fileID, short fileVersion);
}
