package sni.common.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sni.common.exceptions.NotFoundException;
import sni.common.models.dtos.UserAdminPanelDTO;
import sni.common.models.entities.FileEntity;
import sni.common.models.entities.UserEntity;
import sni.common.models.enums.Role;
import sni.common.repositories.FilesRepository;
import sni.common.repositories.UsersRepository;
import sni.common.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    private final UsersRepository usersRepository;
    private final FilesRepository filesRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UsersRepository usersRepository, FilesRepository filesRepository, ModelMapper modelMapper)
    {
        this.usersRepository = usersRepository;
        this.filesRepository = filesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserAdminPanelDTO changeUserPermissions(Integer userID, @Valid UserAdminPanelDTO usr)
    {
        UserEntity user = this.usersRepository.findById(userID).orElseThrow(NotFoundException::new);
        FileEntity rootDir = null;

        if(usr.getRootDirID() != null)
        {
            rootDir = this.filesRepository.findById(usr.getRootDirID()).orElseThrow(NotFoundException::new);
        }
        //FileEntity rootDir = this.filesRepository.findById(usr.getRootDirID()).orElseThrow(NotFoundException::new);


        user.setRole(usr.getRole());
        user.setActive(usr.getActive());
        user.setCanCreate(usr.getCanCreate());
        user.setCanRead(usr.getCanRead());
        user.setCanUpdate(usr.getCanUpdate());
        user.setCanDelete(usr.getCanDelete());
        user.setAccessFromIp(usr.getAccessFromIp());
        user.setAccessFromDomain(usr.getAccessFromDomain());
        user.setRootDir(rootDir);
        user = this.usersRepository.saveAndFlush(user);
        //user.setRootDir(rootDir);

        return usr;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserAdminPanelDTO> getUsers(String containsInUsername, Role role, int page, int pageSize)
    {
        Pageable usersPage = PageRequest.of(page, pageSize);

        List<UserEntity> users = this.usersRepository.filterUsers(containsInUsername, role, usersPage);

        return users
                .stream()
                .map(u -> modelMapper.map(u, UserAdminPanelDTO.class))
                .toList();
    }


}
