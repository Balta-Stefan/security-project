package sni.common.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "files")
public class FileEntity
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "file_id", nullable = false)
    private Integer fileId;

    @Basic
    @Column(name = "name", nullable = false, length = 55)
    private String name;

    @Basic
    @Column(name = "is_directory", nullable = true)
    private Boolean isDirectory;

    @Basic
    @Column(name = "discarded", nullable = false)
    private Boolean discarded;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "affectedFile", fetch = FetchType.LAZY)
    private List<FileLogEntity> fileLogs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "file_id", nullable = false)
    private FileEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<FileEntity> children;

    @OneToMany(mappedBy = "firstVersion", fetch = FetchType.LAZY)
    private List<FileVersionEntity> versions;
}
