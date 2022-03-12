package sni.admin_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import sni.common.services.implementations.CustomOidcUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                    .failureHandler(this.failureHandler());
        /*oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint().oidcUserService(this.customOidcUserService()));*/
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler()
    {
        return new SimpleUrlAuthenticationFailureHandler("email_not_confirmed.html");
    }

    @Bean // this is used by default...
    public CustomOidcUserService customOidcUserService()
    {
        return new CustomOidcUserService();
    }
}
