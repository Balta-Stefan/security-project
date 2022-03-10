package sni.common.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "file_versions")
public class FileVersionEntity
{
    @EmbeddedId
    private FileVersionEntityPK fileVersionEntityPK;

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false, insertable = false, updatable = false)
    private FileEntity firstVersion;
}
