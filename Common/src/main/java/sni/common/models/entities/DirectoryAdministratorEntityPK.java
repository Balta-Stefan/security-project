package sni.common.models.entities;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DirectoryAdministratorEntityPK implements Serializable
{
    private Integer directoryID;
    private Integer userID;
}
