package com.rand.ishowApi.security.jwt;

import com.rand.ishowApi.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger exceptionLogger = LoggerFactory.getLogger("exceptionLogger");

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String code = "TOKEN_EXPIRED";
        String message = "Unauthorized";

        Throwable cause = authException.getCause();
        if (cause != null && cause.getMessage() != null) {
            if (cause.getMessage().contains("expired")) {
                code="TOKEN_EXPIRED";
                message = "TOKEN_EXPIRED";
            } else if (cause.getMessage().contains("JWT") || cause.getMessage().contains("Malformed")) {
                code="TOKEN_INVALID";
                message = "TOKEN_INVALID";
            }
        }

        // Create ApiResponse using your static factory
        exceptionLogger.error("authException occurred: {}", cause.getMessage());

        ApiResponse<Void> apiResponse = ApiResponse.failure(code, new Object[]{message});

        response.setContentType("application/json");
        response.setStatus(200);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));    }
}
