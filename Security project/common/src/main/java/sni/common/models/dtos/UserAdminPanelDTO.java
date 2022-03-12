package sni.common.models.dtos;

import lombok.Data;
import sni.common.models.enums.Role;

import javax.validation.constraints.NotNull;

@Data
public class UserAdminPanelDTO
{
    private Integer id;

    @NotNull
    private Role role;

    @NotNull
    private Boolean active, canCreate, canRead, canUpdate, canDelete;

    private String username;

    private String accessFromIp, accessFromDomain;

    @NotNull
    private Integer rootDirID;
}
