package sni.common.models.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DirectoryAdministratorEntityPK implements Serializable
{
    @Column(name = "dir_id")
    private Integer dirID;

    @Column(name = "user_id")
    private Integer userID;
}
