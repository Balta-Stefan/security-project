package sni.common.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVersionEntityPK implements Serializable
{
    @Column(name = "file_id", nullable = false)
    private Integer file;

    @Column(name = "version", nullable = false)
    private Short version;
}
