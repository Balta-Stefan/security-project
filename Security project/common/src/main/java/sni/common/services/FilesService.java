package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.*;

import java.util.List;
import java.util.Optional;

public interface FilesService
{
    FileDTO createFile(FileDTO toCreate, Resource fileData, int creatorID);
    FileResourceDownloadWrapper readFile(int fileID, Optional<Short> requestedVersion, int askerID);
    FileDTO updateFile(int fileID, Resource fileData, int askerID);
    void deleteFile(int fileID, int askerID);

    FileDTO moveFile(int fileID, int newParentID, int askerID);
    FileDTO renameFile(int fileID, int askerID, String newName);

    DirectoryDTO listDir(int fileID, int askerID);
    FileDTO createDir(FileDTO toCreate, int creatorID);

    List<FileLogDTO> getLogs(int fileID);

    DirectoryDTO getRoot(int userID);
    //List<DirectoryDTO> getBreadCrumbs(int directoryID, int askerID);
}
