package sni.admin_service.controllers;

import org.springframework.web.bind.annotation.*;
import sni.common.models.dtos.UserAdminPanelDTO;
import sni.common.services.UserService;


@RestController
@RequestMapping("/api/v1/user")
public class UsersController
{
    private final UserService userService;

    public UsersController(UserService userService)
    {
        this.userService = userService;
    }

    @PutMapping("/{userID}")
    public UserAdminPanelDTO changeUserInfo(@PathVariable Integer userID, @RequestBody UserAdminPanelDTO userDTO)
    {
        return this.userService.changeUserPermissions(userID, userDTO);
    }
}
