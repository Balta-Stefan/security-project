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
    @Column(name = "first_file", nullable = false)
    private Integer firstFile;

    @Column(name = "version", nullable = false)
    private Short version;
}
