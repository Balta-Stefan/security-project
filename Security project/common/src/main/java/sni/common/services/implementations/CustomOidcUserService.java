package sni.common.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sni.common.exceptions.ForbiddenException;
import sni.common.models.CustomOidcUser;
import sni.common.models.entities.UserEntity;
import sni.common.models.enums.Permission;
import sni.common.models.enums.Role;
import sni.common.repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class CustomOidcUserService extends OidcUserService
{
    @Autowired
    private UsersRepository usersRepository;


    private CustomOidcUser handleUserLogin(OidcUser user)
    {
        URL issuerURL = (URL) user.getIdToken().getClaims().get("iss");

        String issuer = issuerURL.getHost();
        String subject = (String) user.getIdToken().getClaims().get("sub");
        String username = (String) user.getIdToken().getClaims().get("nickname");

        Optional<UserEntity> userEntityOptional = this.usersRepository.findByOidcIssAndOidcSub(issuer, subject);

        UserEntity userEntity;

        // authorities cannot be simply added to user.getAuthorities() because Collection<? extends GrantedAuthority> cannot receive new objects
        List<GrantedAuthority> userAuthorities = new ArrayList<>(user.getAuthorities());

        if(userEntityOptional.isPresent())
        {
            // the user already exists
            userEntity = userEntityOptional.get();

            GrantedAuthority userRole = new SimpleGrantedAuthority(userEntity.getRole().name());
            userAuthorities.add(userRole);

            if(userEntityOptional.get().getCanCreate() == true)
            {
                userAuthorities.add(new SimpleGrantedAuthority(Permission.CREATE.name()));
            }
            if(userEntityOptional.get().getCanRead() == true)
            {
                userAuthorities.add(new SimpleGrantedAuthority(Permission.READ.name()));
            }
            if(userEntityOptional.get().getCanUpdate() == true)
            {
                userAuthorities.add(new SimpleGrantedAuthority(Permission.UPDATE.name()));
            }
            if(userEntityOptional.get().getCanDelete() == true)
            {
                userAuthorities.add(new SimpleGrantedAuthority(Permission.DELETE.name()));
            }
        }
        else
        {
            // a new user has been authenticated
            userEntity = new UserEntity();
            userEntity.setEmail(user.getEmail());
            userEntity.setUsername(username);
            userEntity.setRole(Role.USER);
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setActive(true); // might need to check if email has been activated
            userEntity.setOidcIss(issuer);
            userEntity.setOidcSub(subject);
            userEntity.setCanCreate(false);
            userEntity.setCanRead(false);
            userEntity.setCanUpdate(false);
            userEntity.setCanDelete(false);

            userEntity = usersRepository.saveAndFlush(userEntity);
        }
        // (Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo)
        CustomOidcUser customOidcUser = new CustomOidcUser(userAuthorities, user.getIdToken(), user.getUserInfo());
        customOidcUser.setUserID(userEntity.getUserId());
        customOidcUser.setRole(userEntity.getRole());
        customOidcUser.setAccessFromDomain(userEntity.getAccessFromDomain());
        customOidcUser.setAccessFromIp(userEntity.getAccessFromIp());
        customOidcUser.setActive(userEntity.getActive());

        return customOidcUser;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException
    {
        OidcUser user = super.loadUser(userRequest);
        try
        {
            return this.handleUserLogin(user);
        }
        catch(Exception e)
        {
            log.warn("CustomOidcUserService has thrown an exception: ", e);
            throw new OAuth2AuthenticationException("CustomOidcUserService exception");
        }
    }


}
