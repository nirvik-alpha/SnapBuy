package com.snapBuy.project.security.jwt;


import com.snapBuy.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Logger for JWT-related events and validation errors
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Secret key used to sign and verify JWT tokens
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    // Token expiration time in milliseconds
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;


    // Name of the cookie used to store JWT
    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {

        // Retrieve JWT cookie from the incoming request
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);

        // Return token value if cookie exists
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String getJwtFromHeader(HttpServletRequest request) {

        // Read Authorization header from request
        String bearerToken = request.getHeader("Authorization");

        // Extract token if header follows Bearer scheme
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {

        // Generate JWT token for authenticated user
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());

        // Create response cookie containing JWT token
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .secure(false)
                .build();

        return cookie;
    }

    public ResponseCookie getCleanJwtCookie() {

        // Create an empty cookie to clear the existing JWT
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
        return cookie;
    }

    public String generateTokenFromUsername(String username) {

        // Build and sign a JWT token containing username as subject
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {

        // Parse JWT and extract username from token subject
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key() {

        // Generate signing key from Base64 encoded secret
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {

            // Verify token signature and validate token structure
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);

            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}