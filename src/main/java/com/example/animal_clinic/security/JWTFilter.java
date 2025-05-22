package com.example.animal_clinic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    public JWTFilter(JWTUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

       final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                String username = jwtUtils.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = customUserDetailsService.loadUserByUsername(username);
                    if (jwtUtils.validateToken(token, user)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }
       filterChain.doFilter(request, response);
    }
}
