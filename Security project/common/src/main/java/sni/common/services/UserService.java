package sni.common.services;

import sni.common.models.dtos.UserAdminPanelDTO;

public interface UserService
{
    UserAdminPanelDTO changeUserPermissions(Integer userID, UserAdminPanelDTO newData);
}
