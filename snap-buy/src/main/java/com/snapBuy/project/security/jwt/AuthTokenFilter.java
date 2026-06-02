package com.snapBuy.project.security.jwt;


import com.snapBuy.project.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter that intercepts every incoming request,
 * validates the JWT token, and establishes the authenticated user
 * in the Spring Security context.
 */

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    // Utility class for JWT generation, validation, and extraction
    @Autowired
    private JwtUtils jwtUtils;

    // Service used to load user details from the database
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // Logger for authentication and debugging information
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    /**
     * Processes each incoming request by extracting and validating the JWT.
     * If the token is valid, the authenticated user is stored in the
     * SecurityContext so that Spring Security can authorize requests.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Log the URI of the incoming request
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());

        try {

            // Extract JWT token from cookie or Authorization header
            String jwt = parseJwt(request);

            // Validate token before processing authentication
            if (jwt != null && jwtUtils.validateJwtToken(jwt))
            {
                // Extract username from the validated token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Load user details associated with the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create authentication object containing user information and authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                // Log user roles extracted from JWT
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                // Attach request-specific details to the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store authenticated user in Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        // Continue processing the remaining filter chain
        filterChain.doFilter(request, response);
    }

//    private String parseJwt(HttpServletRequest request) {
//        String jwt = jwtUtils.getJwtFromCookies(request);
//        logger.debug("AuthTokenFilter.java: {}", jwt);
//        return jwt;
//    }


    /**
     * Retrieves JWT token from either cookies or the Authorization header.
     * Cookie-based authentication is checked first, followed by header-based authentication.
     */

    private String parseJwt(HttpServletRequest request) {

        // Attempt to retrieve JWT from cookies
        String jwtFromCookie = jwtUtils.getJwtFromCookies(request);

        // Return token if found in cookies
        if (jwtFromCookie != null) {
            return jwtFromCookie;
        }

        String jwtFromHeader = jwtUtils.getJwtFromHeader(request);

        if (jwtFromHeader != null) {
            return jwtFromHeader;
        }

        return null;
    }
}