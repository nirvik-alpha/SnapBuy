package com.snapBuy.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles unauthorized access attempts to secured resources.
 * Invoked by Spring Security when authentication fails or
 * when a user tries to access a protected endpoint without
 * valid credentials.
 */

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    // Logger for recording authentication-related errors
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);


    /**
     * Generates a custom HTTP 401 Unauthorized response.
     * This method is automatically triggered by Spring Security
     * whenever an authentication exception occurs.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Log unauthorized access attempt with exception message
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Set response content type as JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Set HTTP status code to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create response body to return error details
        final Map<String, Object> body = new HashMap<>();

        // Add HTTP status code
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        // Add error type
        body.put("error", "Unauthorized");

        // Add exception message
        body.put("message", authException.getMessage());

        // Add requested endpoint path
        body.put("path", request.getServletPath());

        // Convert response body map to JSON
        final ObjectMapper mapper = new ObjectMapper();

        // Write JSON response to output stream
        mapper.writeValue(response.getOutputStream(), body);
    }

}