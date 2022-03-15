package sni.common.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DirectoryBasicDTO
{
    private int fileId;
    private int parentID;
    @NotBlank
    private String name;
}
