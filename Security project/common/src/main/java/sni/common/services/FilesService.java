package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.DirectoryDTO;
import sni.common.models.dtos.FileDTO;

import java.util.List;
import java.util.Optional;

public interface FilesService
{
    FileDTO createFile(FileDTO toCreate, Resource fileData, int creatorID);
    Resource readFile(int fileID, short requestedVersion, int askerID);
    FileDTO updateFile(int fileID, Resource fileData, int askerID);
    void deleteFile(int fileID, int askerID);

    FileDTO moveFile(int fileID, int newParentID, int askerID);
    FileDTO renameFile(int fileID, int askerID, String newName);

    List<DirectoryDTO> listDir(int fileID, int askerID);
    FileDTO createDir(FileDTO toCreate, int creatorID);
}
