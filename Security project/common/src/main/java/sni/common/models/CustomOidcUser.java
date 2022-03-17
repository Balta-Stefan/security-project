package sni.common.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import sni.common.models.enums.Role;

import java.util.Collection;

public class CustomOidcUser extends DefaultOidcUser
{
    private int userID;
    private Role role;
    private String accessFromDomain;
    private String accessFromIp;
    private boolean active;

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getAccessFromDomain()
    {
        return accessFromDomain;
    }

    public void setAccessFromDomain(String accessFromDomain)
    {
        this.accessFromDomain = accessFromDomain;
    }

    public String getAccessFromIp()
    {
        return accessFromIp;
    }

    public void setAccessFromIp(String accessFromIp)
    {
        this.accessFromIp = accessFromIp;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken)
    {
        super(authorities, idToken);
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, String nameAttributeKey)
    {
        super(authorities, idToken, nameAttributeKey);
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo)
    {
        super(authorities, idToken, userInfo);
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo, String nameAttributeKey)
    {
        super(authorities, idToken, userInfo, nameAttributeKey);
    }
}
