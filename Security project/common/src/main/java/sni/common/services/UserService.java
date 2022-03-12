package sni.common.services;

import sni.common.models.dtos.UserAdminPanelDTO;
import sni.common.models.enums.Role;

import java.util.List;

public interface UserService
{
    UserAdminPanelDTO changeUserPermissions(Integer userID, UserAdminPanelDTO newData);
    List<UserAdminPanelDTO> getUsers(String containsInUsername, Role role, int page, int pageSize);
}
