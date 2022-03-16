package sni.common.models.entities;

import lombok.*;
import sni.common.models.enums.Operation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "file_logs")
public class FileLogEntity
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "log_id", nullable = false)
    private Long logId;

    @Basic
    @Column(name = "description", length = 255)
    private String description;

    @Basic
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Basic
    @Column(name = "operation", nullable = false)
    private Operation operation;

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false)
    private FileEntity affectedFile;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity createdBy;

}
