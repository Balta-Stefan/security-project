package sni.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import sni.common.models.CustomOidcUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException
    {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        boolean invalidateSession = false;

        CustomOidcUser user = (CustomOidcUser)authentication.getPrincipal();
        if(user.getAccessFromDomain() != null)
        {
            if(user.getAccessFromDomain().equals(request.getRemoteAddr()) == false)
                invalidateSession = true;
        }
        else if(user.getAccessFromIp() != null)
        {
            if(user.getAccessFromIp().equals(request.getRemoteHost()) == false)
                invalidateSession = true;
        }

        if(invalidateSession == true)
        {
            request.getSession().invalidate();
            SecurityContext context = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            context.setAuthentication(null);

            Cookie jsessionIdCookie = new Cookie("JSESSIONID", null);
            jsessionIdCookie.setPath("/");
            jsessionIdCookie.setMaxAge(0);
            response.addCookie(jsessionIdCookie);

            response.sendRedirect("/forbidden.html");
            return;
        }

        Cookie roleCookie = new Cookie("role", user.getRole().name());
        roleCookie.setPath("/");
        response.addCookie(roleCookie);
        response.sendRedirect("/");
    }
}
