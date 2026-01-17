package com.su.caremomsbackend.auth;

import com.su.caremomsbackend.model.User;
import com.su.caremomsbackend.service.TokenValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidationService tokenValidationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String adminHeader = request.getHeader("Admin");
        String token = null;
        String personRole = null;
        String personId = null;
        String personName = null;

        // Extract JWT token from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                personRole = tokenValidationService.extractRole(token);
                personId = tokenValidationService.extractPersonId(token);
                personName = tokenValidationService.extractName(token);
            } catch (Exception e) {
                // Invalid token
                filterChain.doFilter(request, response);
                return;
            }
        }
        User adminUser=null;
        if(adminHeader !=null){
            adminUser = tokenValidationService.getAdminUser(request);
            personRole= adminUser.getRole();
            personId = String.valueOf(adminUser.getId());
            personName = adminUser.getUserName();
        }

        // Validate token and set authentication
        if (personId != null && personName != null && personRole != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Map<String, String> principal = Map.of("personId", personId, "personName", personName);
            if (tokenValidationService.validateToken(request) || adminUser !=null ) {
                // Create authentication object with role
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + personRole.toUpperCase()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request, response);
    }
}