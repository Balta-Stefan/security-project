package sni.admin_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sni.common.services.implementations.CustomOidcUserService;
import sni.common.utils.CustomAuthSuccessHandler;
import sni.common.utils.CustomLogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminServiceSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final CustomOidcUserService customOidcUserService;
    private final CustomLogoutHandler customLogoutHandler;

    public AdminServiceSecurityConfig(CustomOidcUserService customOidcUserService, CustomLogoutHandler customLogoutHandler)
    {
        this.customOidcUserService = customOidcUserService;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .anyRequest().hasAuthority("ADMIN")
                .and()
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint()
                                .oidcUserService(this.customOidcUserService)
                                .and()
                                .successHandler(this.customAuthSuccessHandler()))
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).addLogoutHandler(this.customLogoutHandler)
                .and()
                .headers().contentSecurityPolicy("default-src 'self';" +
                        "script-src 'self';" +
                        "style-src 'self' https://fonts.googleapis.com https://cdn.jsdelivr.net 'unsafe-inline';" +
                        "font-src https://fonts.gstatic.com");

            // angular material doesn't work without inline styles
    }

    @Bean
    public CustomAuthSuccessHandler customAuthSuccessHandler()
    {
        return new CustomAuthSuccessHandler();
    }
    /*@Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults()
    {
        return new GrantedAuthorityDefaults("");
    }*/
    /*@Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler()
    {
        return new SimpleUrlAuthenticationFailureHandler("email_not_confirmed.html");
    }*/
}
