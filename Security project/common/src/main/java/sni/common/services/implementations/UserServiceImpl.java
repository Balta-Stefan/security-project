package sni.common.services.implementations;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sni.common.exceptions.NotFoundException;
import sni.common.models.dtos.UserAdminPanelDTO;
import sni.common.models.entities.FileEntity;
import sni.common.models.entities.UserEntity;
import sni.common.repositories.FilesRepository;
import sni.common.repositories.UsersRepository;
import sni.common.services.UserService;

import javax.validation.Valid;

@Service
public class UserServiceImpl implements UserService
{
    private final UsersRepository usersRepository;
    private final FilesRepository filesRepository;

    public UserServiceImpl(UsersRepository usersRepository, FilesRepository filesRepository)
    {
        this.usersRepository = usersRepository;
        this.filesRepository = filesRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserAdminPanelDTO changeUserPermissions(Integer userID, @Valid UserAdminPanelDTO usr)
    {
        UserEntity user = this.usersRepository.findById(userID).orElseThrow(NotFoundException::new);
        FileEntity rootDir = this.filesRepository.findById(usr.getRootDirID()).orElseThrow(NotFoundException::new);


        user.setRole(usr.getRole());
        user.setActive(usr.getActive());
        user.setCanCreate(usr.getCanCreate());
        user.setCanRead(usr.getCanRead());
        user.setCanUpdate(usr.getCanUpdate());
        user.setCanDelete(usr.getCanDelete());
        user.setAccessFromIp(usr.getAccessFromIp());
        user.setAccessFromDomain(usr.getAccessFromDomain());
        user.setRootDir(rootDir);

        return usr;
    }


}
