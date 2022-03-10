package sni.common.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import sni.common.models.CustomOidcUser;
import sni.common.models.entities.UserEntity;
import sni.common.models.enums.Role;
import sni.common.repositories.UsersRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class CustomOidcUserService extends OidcUserService
{
    private final UsersRepository usersRepository;

    public CustomOidcUserService(UsersRepository usersRepository)
    {
        this.usersRepository = usersRepository;
    }

    private CustomOidcUser handleUserLogin(OidcUser user)
    {
        String issuer = (String) user.getIdToken().getClaims().get("iss");
        String subject = (String) user.getIdToken().getClaims().get("sub");

        Optional<UserEntity> userEntityOptional = this.usersRepository.findByOidcIssAndOidcSub(issuer, subject);
        // (Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo)
        CustomOidcUser customOidcUser = new CustomOidcUser(user.getAuthorities(), user.getIdToken(), user.getUserInfo(), user.getName());

        UserEntity userEntity;
        if(userEntityOptional.isPresent())
        {
            // the user already exists
            userEntity = userEntityOptional.get();
        }
        else
        {
            // a new user has been authenticated
            userEntity = new UserEntity();
            userEntity.setEmail(user.getEmail());
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
        customOidcUser.setUserID(userEntity.getUserId());

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
