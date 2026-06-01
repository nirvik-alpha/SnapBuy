package com.snapBuy.project.controller;


import com.snapBuy.project.config.AppConstants;
import com.snapBuy.project.payload.AuthenticationResult;
import com.snapBuy.project.security.request.LoginRequest;
import com.snapBuy.project.security.request.SignupRequest;
import com.snapBuy.project.security.response.MessageResponse;
import com.snapBuy.project.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/* * Authentication Controller *
*  * Responsible for:
*  - User registration (signup)
*  - User authentication (signin)
*  - User logout (signout)
*  - Retrieving authenticated user details
*  - Retrieving seller accounts with pagination
*  */


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Service layer responsible for authentication and user management

    @Autowired
    AuthService authService;


    /**
     * Authenticate user credentials and generate JWT token.
     * The JWT token is returned as a secure HttpOnly cookie.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        result.getJwtCookie().toString())
                .body(result.getResponse());
    }

    /**
     * Register a new user or seller account.
     * Performs validation before account creation.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    /**
     * Retrieve the username of the currently authenticated user.
     */
    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }

    /**
     * Retrieve complete profile information of the currently authenticated user.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));
    }

    /**
     * Logout the current user.
     * Removes the authentication cookie from the client.
     */
    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = authService.logoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    /**
     * Retrieve all seller accounts with pagination support.
     */
    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber ,
                Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }

}
