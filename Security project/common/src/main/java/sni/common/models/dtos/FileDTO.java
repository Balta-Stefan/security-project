package sni.common.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FileDTO
{
    private Integer fileId;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isDirectory;
    private Boolean discarded;
    private Boolean deleted;

    @NotNull
    private Integer parent; // must be manually mapped
    private Short numOfVersions;
    private List<FileVersionDTO> fileVersions;
}
