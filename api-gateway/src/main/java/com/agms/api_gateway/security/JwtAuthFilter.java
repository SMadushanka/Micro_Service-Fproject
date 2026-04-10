package com.agms.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // If a Bearer token is present, validate it and populate the SecurityContext.
        // If no token is present (e.g. /auth/login), just continue — Spring Security's
        // authorization rules (permitAll / authenticated) decide what to allow.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String subject = jwtUtil.validateAndGetSubject(token);
                log.debug("JWT valid for subject: {}", subject);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(subject, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                log.warn("JWT validation failed: {}", e.getMessage());
                // Do not set authentication — Spring Security will reject if endpoint is secured
            }
        }

        filterChain.doFilter(request, response);
    }
}
