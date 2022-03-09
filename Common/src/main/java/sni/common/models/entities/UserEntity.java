package sni.common.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
public class UserEntity
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "email", nullable = false, length = 55)
    private String email;

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Basic
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Basic
    @Column(name = "oidc_iss", nullable = true, length = 512)
    private String oidcIss;
	
	@Basic
	@Column(name = "oidc_sub", nullable = true, length = 255)
	private String oidcSub;

    @Basic
    @Column(name = "can_create", nullable = false)
    private Boolean canCreate;

    @Basic
    @Column(name = "can_read", nullable = false)
    private Boolean canRead;

    @Basic
    @Column(name = "can_update", nullable = false)
    private Boolean canUpdate;

    @Basic
    @Column(name = "can_delete", nullable = false)
    private Boolean canDelete;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<FileLogEntity> createdLogs;
}
