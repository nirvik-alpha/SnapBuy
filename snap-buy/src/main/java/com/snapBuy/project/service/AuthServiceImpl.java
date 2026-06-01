package com.snapBuy.project.service;

import com.snapBuy.project.model.AppRole;
import com.snapBuy.project.model.Role;
import com.snapBuy.project.model.User;
import com.snapBuy.project.payload.AuthenticationResult;
import com.snapBuy.project.payload.UserDTO;
import com.snapBuy.project.payload.UserResponse;
import com.snapBuy.project.repositories.RoleRepository;
import com.snapBuy.project.repositories.UserRepository;
import com.snapBuy.project.security.jwt.JwtUtils;
import com.snapBuy.project.security.request.LoginRequest;
import com.snapBuy.project.security.request.SignupRequest;
import com.snapBuy.project.security.response.MessageResponse;
import com.snapBuy.project.security.response.UserInfoResponse;
import com.snapBuy.project.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementation of AuthService.
 * Handles authentication logic, JWT token generation,
 * user registration, role assignment, and seller retrieval.
 */

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    // Manages an authentication process
    @Autowired
    private AuthenticationManager authenticationManager;


    // Utility class for JWT token generation and validation
    @Autowired
    private JwtUtils jwtUtils;

    // Repository for user data access
    @Autowired
    UserRepository userRepository;

    // Repository for role management
    @Autowired
    RoleRepository roleRepository;

    // Password encoder for secure password hashing
    @Autowired
    PasswordEncoder encoder;

    // Used for DTO mapping between Entity and Response objects
    @Autowired
    ModelMapper modelMapper;


    /**
     * Authenticate user credentials and generate JWT token.
     */
    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {

        // Authenticate user credentials
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );


        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get authenticated user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate JWT cookie
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // Extract roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Build response DTO
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());

        return new AuthenticationResult(response, jwtCookie);
    }

    /**
     * Register a new user with role assignment.
     */
    @Override
    public ResponseEntity<MessageResponse> register(SignupRequest signUpRequest) {

        // Check duplicate username
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check duplicate email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user with encrypted password
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        // Assign roles
        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * Get details of currently logged-in user from security context.
     */
    @Override
    public UserInfoResponse getCurrentUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles);

        return response;
    }

    /**
     * Logout user by clearing JWT cookie from client.
     */
    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    /**
     * Retrieve paginated list of all sellers.
     */
    @Override
    public UserResponse getAllSellers(Pageable pageable) {
        Page<User> allUsers = userRepository.findByRoleName(AppRole.ROLE_SELLER, pageable);

        // Convert an entity list to a DTO list
        List<UserDTO> userDtos = allUsers.getContent()
                .stream()
                .map(p -> modelMapper.map(p, UserDTO.class))
                .collect(Collectors.toList());

        // Build paginated response
        UserResponse response = new UserResponse();
        response.setContent(userDtos);
        response.setPageNumber(allUsers.getNumber());
        response.setPageSize(allUsers.getSize());
        response.setTotalElements(allUsers.getTotalElements());
        response.setTotalPages(allUsers.getTotalPages());
        response.setLastPage(allUsers.isLast());
        return response;
    }


}