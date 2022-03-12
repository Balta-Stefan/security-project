package sni.common.models.dtos;

import lombok.Data;

@Data
public class FileBasicDTO
{
    private Integer fileId;
    private String name;
    private Boolean isDirectory;
}
