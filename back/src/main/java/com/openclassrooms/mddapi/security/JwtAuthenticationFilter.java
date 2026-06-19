package com.openclassrooms.mddapi.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("===== JWT FILTER =====");

        String header =
                request.getHeader("Authorization");
        System.out.println("Header = " + header);
        if (header != null &&
                header.startsWith("Bearer ")) {

            String token = header.substring(7);

            System.out.println("Token reçu");

            boolean valid = jwtService.isValid(token);

            System.out.println("Token valide = " + valid);

            if (valid) {

                String username =
                        jwtService.extractUsername(token);

                System.out.println("Username = " + username);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                AuthorityUtils.NO_AUTHORITIES
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);

                System.out.println(
                        "Authentication = "
                                + SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                );
            }
        }
        filterChain.doFilter(request, response);
    }
}
