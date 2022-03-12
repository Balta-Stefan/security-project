package sni.common.models.dtos;

import lombok.Data;
import sni.common.models.enums.Operation;

import java.time.LocalDateTime;

@Data
public class FileLogDTO
{
    private Long logId;
    private String description;
    private LocalDateTime timestamp;
    private Operation operation;
    private FileBasicDTO affectedFile;
    private UserBasicDTO createdBy;
}
