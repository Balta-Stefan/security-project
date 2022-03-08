package sni.common.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDTO
{
    private Integer fileId;
    private String name;
    private Boolean isDirectory;
    private LocalDateTime createdAt;
    private Boolean discarded;
    private Boolean deleted;
    private Integer parent; // must be manually mapped
    private Integer createdBy; // must be manually mapped
}
