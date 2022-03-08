package sni.common.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "directory_administrators")
public class DirectoryAdministratorEntity
{
    @EmbeddedId
    private DirectoryAdministratorEntityPK administrationID;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "create", nullable = false)
    private Boolean create;

    @Basic
    @Column(name = "read", nullable = false)
    private Boolean read;

    @Basic
    @Column(name = "update", nullable = false)
    private Boolean update;

    @Basic
    @Column(name = "delete", nullable = false)
    private Boolean delete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dir_id", referencedColumnName = "file_id", nullable = false)
    private FileEntity directory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity administrator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointed_by", referencedColumnName = "user_id", nullable = false)
    private UserEntity appointedBy;

}
