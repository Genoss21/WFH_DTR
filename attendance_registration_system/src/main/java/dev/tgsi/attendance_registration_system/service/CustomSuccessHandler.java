package dev.tgsi.attendance_registration_system.service;

import java.io.IOException;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Extracting the authorities (roles) from authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        // Finding the first role
        String role = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse("");

        // Redirect based on the role
        if ("SysAdmin".equals(role)) {
            response.sendRedirect("/admin-page");
        } else if ("User".equals(role)) {
            response.sendRedirect("/user-page");
        } else {
            // Log the unexpected role for debugging
            System.err.println("Unexpected role: " + role);
            response.sendRedirect("/error");
        }
    }
}
