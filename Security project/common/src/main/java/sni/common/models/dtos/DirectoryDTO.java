package sni.common.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectoryDTO
{
    private List<DirectoryBasicDTO> breadCrumbs;
    private DirectoryBasicDTO directory;
    private List<FileBasicDTO> children;
}
