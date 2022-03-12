package sni.common.models.entities;

import lombok.*;
import sni.common.models.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "username", nullable = false, length = 55)
    private String username;

    @Basic
    @Column(name = "role", nullable = false, length = 45)
    private Role role;

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

    @Basic
    @Column(name = "access_from_ip", length = 15)
    private String accessFromIp;

    @Basic
    @Column(name = "access_from_domain", length = 255)
    private String accessFromDomain;

    @ManyToOne
    @JoinColumn(name = "root_dir_id", referencedColumnName = "file_id")
    private FileEntity rootDir;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<FileLogEntity> createdLogs;
}
