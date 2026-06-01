package com.snapBuy.project.service;

import com.snapBuy.project.payload.AuthenticationResult;
import com.snapBuy.project.payload.UserResponse;
import com.snapBuy.project.security.request.LoginRequest;
import com.snapBuy.project.security.request.SignupRequest;
import com.snapBuy.project.security.response.MessageResponse;
import com.snapBuy.project.security.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


/**
 * Service interface for authentication and user management.
 * Handles:
 * - User login
 * - User registration
 * - Fetching current user details
 * - Logout functionality
 * - Retrieving a seller list with pagination
 */
public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> register(SignupRequest signUpRequest);

    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    /**
     * Logout user by clearing JWT cookie.
     */
    ResponseCookie logoutUser();

    UserResponse getAllSellers(Pageable pageable);
}