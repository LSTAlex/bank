package com.example.bank.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication  authentication) throws IOException, ServletException {

        boolean isAdmin = false; //1 - ADMIN, 0 - USER

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                isAdmin = true;
                break;
            } else if (authority.getAuthority().equals("USER")) {
                isAdmin = false;
                break;
            }
        }

        if (isAdmin) {
            getRedirectStrategy().sendRedirect(request, response, "/homeadmin");
        }
        else if (!isAdmin){
            getRedirectStrategy().sendRedirect(request, response, "/homeuser");
        }
        else {
            throw new IllegalStateException();
        }
    }
}
