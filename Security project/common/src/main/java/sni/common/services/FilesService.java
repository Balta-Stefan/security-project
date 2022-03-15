package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.*;

import java.util.List;
import java.util.Optional;

public interface FilesService
{
    FileBasicDTO createFile(int parentID, Resource fileData, int creatorID);
    FileResourceDownloadWrapper readFile(int fileID, Optional<Short> requestedVersion, int askerID);
    FileDTO updateFile(int fileID, Resource fileData, int askerID);
    void deleteFile(int fileID, int askerID);

    void moveFile(int fileID, int newParentID, int askerID);
    void renameFile(int fileID, int askerID, String newName);

    DirectoryDTO listDir(int fileID, int askerID);
    FileDTO createDir(int parentID, String name, int creatorID);

    List<FileLogDTO> getLogs(int fileID);

    DirectoryDTO getRoot(int userID);
    //List<DirectoryDTO> getBreadCrumbs(int directoryID, int askerID);
}
