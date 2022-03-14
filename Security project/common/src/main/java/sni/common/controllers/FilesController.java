package sni.common.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sni.common.exceptions.BadRequestException;
import sni.common.models.CustomOidcUser;
import sni.common.models.dtos.*;
import sni.common.services.FilesService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/files")
public class FilesController
{
    private final FilesService filesService;

    public FilesController(FilesService filesService)
    {
        this.filesService = filesService;
    }

    @GetMapping("/dir/{dirID}")
    public DirectoryDTO listDir(@PathVariable Integer dirID, @AuthenticationPrincipal CustomOidcUser principal)
    {
        return this.filesService.listDir(dirID, principal.getUserID());
    }

    /*@GetMapping("/dir/{dirID}/breadcrumbs")
    public List<DirectoryDTO> getBreadCrumbs(@PathVariable Integer dirID, @AuthenticationPrincipal CustomOidcUser principal)
    {
        return this.filesService.getBreadCrumbs(dirID, principal.getUserID());
    }*/

    @GetMapping("/{fileID}/logs")
    public List<FileLogDTO> getLogs(@PathVariable Integer fileID)
    {
        return this.filesService.getLogs(fileID);
    }

    @GetMapping("/root")
    public DirectoryDTO getRoot(@AuthenticationPrincipal CustomOidcUser principal)
    {
        return this.filesService.getRoot(principal.getUserID());
    }

    @PostMapping("/file")
    public FileDTO createFile(@RequestBody FileDTO fileDTO, MultipartFile file, @AuthenticationPrincipal CustomOidcUser principal)
    {
        if(fileDTO.getIsDirectory() == true)
            return this.filesService.createDir(fileDTO, principal.getUserID());
        else if(file == null)
            throw new BadRequestException();

        return this.filesService.createFile(fileDTO, file.getResource(), principal.getUserID());
    }


    @DeleteMapping("/file/{fileID}")
    public void deleteFile(@PathVariable Integer fileID, @AuthenticationPrincipal CustomOidcUser principal)
    {
        this.filesService.deleteFile(fileID, principal.getUserID());
    }

    @GetMapping("/file/{fileID}")
    public ResponseEntity<Resource> downloadLatestVersionOfFile(@PathVariable Integer fileID, @AuthenticationPrincipal CustomOidcUser principal)
    {
        // download the latest version
        FileResourceDownloadWrapper fileWrapper = filesService.readFile(fileID, Optional.empty(), principal.getUserID());
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileWrapper.getFileName())
                .build();//builder("inline").filename(fileWrapper.getFileName()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileWrapper.getFile());

        /*return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);*/
    }

    @GetMapping("/file/{fileID}/version/{version}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileID, @PathVariable Short version, @AuthenticationPrincipal CustomOidcUser principal)
    {
        FileResourceDownloadWrapper fileWrapper = filesService.readFile(fileID, Optional.of(version), principal.getUserID());

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileWrapper.getFileName())
                .build();//builder("inline").filename(fileWrapper.getFileName()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileWrapper.getFile());

        /*return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);*/
    }

    @PatchMapping("/file/{fileID}/parent/{newParentID}")
    public FileDTO changeParent(@PathVariable Integer fileID, @PathVariable Integer newParentID, @AuthenticationPrincipal CustomOidcUser principal)
    {
        return filesService.moveFile(fileID, newParentID, principal.getUserID());
    }

    @PatchMapping("/file/{fileID}/name/{newName}")
    public FileDTO changeName(@PathVariable Integer fileID, @PathVariable String newName, @AuthenticationPrincipal CustomOidcUser principal)
    {
        return filesService.renameFile(fileID, principal.getUserID(), newName);
    }

    @PostMapping("/file/{fileID}")
    public FileDTO updateFile(@PathVariable Integer fileID, MultipartFile file, @AuthenticationPrincipal CustomOidcUser principal)
    {
        return filesService.updateFile(fileID, file.getResource(), principal.getUserID());
    }
}
