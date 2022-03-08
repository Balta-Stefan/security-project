package sni.common.services;

import org.springframework.core.io.Resource;
import sni.common.models.dtos.FileDTO;

public interface FilesService
{
    FileDTO createDirectory(int parentID, int creatorID);
    FileDTO createFile(int parentID, int creatorID, Resource file);
    FileDTO moveFile(int fileID, int newParentID, int applicantUserID);
    void deleteFile(int fileID, int applicantUserID);
}
