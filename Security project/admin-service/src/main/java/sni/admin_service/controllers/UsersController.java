package sni.admin_service.controllers;

import org.springframework.web.bind.annotation.*;
import sni.common.models.dtos.UserAdminPanelDTO;
import sni.common.models.enums.Role;
import sni.common.services.UserService;

import java.util.List;


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

    @GetMapping()
    public List<UserAdminPanelDTO> getUsers(@RequestParam(required = false) String username,
                                            @RequestParam(required = false) Role role,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "20") Integer pageSize)
    {
        return this.userService.getUsers(username, role, page, pageSize);
    }
}
