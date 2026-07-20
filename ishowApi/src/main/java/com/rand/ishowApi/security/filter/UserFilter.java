package com.rand.ishowApi.security.filter;

import com.rand.ishowApi.security.context.UserContext;
import com.rand.ishowApi.security.jwt.JwtAuthConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserFilter  extends OncePerRequestFilter {
    private final JwtAuthConverter jwtAuthConverter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken token) {
                Jwt jwt = token.getToken();

                // Extract all custom claims
                Map<String, Object> customClaims = jwtAuthConverter.extractCustomClaims(jwt);

                // Set claims in thread-local context
                UserContext.setUserClaims(customClaims);
            }

            chain.doFilter(req, res);

        } finally {
            // Clear context after request
           UserContext.clear();
        }
    }

}
