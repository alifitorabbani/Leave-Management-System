package com.len.leave.manual.service;

import com.len.leave.manual.dto.AuthResponse;
import com.len.leave.manual.dto.LoginRequest;
import com.len.leave.manual.dto.RegisterRequest;
import com.len.leave.manual.dto.UserResponse;
import com.len.leave.manual.entity.User;
import com.len.leave.manual.repository.UserRepository;
import com.len.leave.manual.security.CustomUserDetails;
import com.len.leave.manual.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Service
 * 
 * Handles user registration, login, and JWT token generation.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {} with role: {}", request.getUsername(), request.getRole());

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: Username already exists: {}", request.getUsername());
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        // Check if employee ID already exists (if provided)
        if (request.getEmployeeId() != null && !request.getEmployeeId().isEmpty()) {
            if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
                log.warn("Registration failed: Employee ID already exists: {}", request.getEmployeeId());
                throw new RuntimeException("Employee ID already exists");
            }
        }

        log.debug("Creating user with role: {}", request.getRole());

        // Validate role
        if (request.getRole() == null) {
            log.error("Registration failed: Role is null");
            throw new RuntimeException("Role is required");
        }
        
        log.debug("Role validation passed: {}", request.getRole());

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .role(request.getRole())
                .isActive(true)
                .build();

        log.debug("User object created, role: {}", user.getRole());
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {} with role: {}", savedUser.getUsername(), savedUser.getRole());

        // Generate token
        CustomUserDetails userDetails = CustomUserDetails.build(savedUser);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", savedUser.getRole().name());
        extraClaims.put("fullName", savedUser.getFullName());
        extraClaims.put("email", savedUser.getEmail());

        String token = jwtUtils.generateToken(userDetails, extraClaims);

        return mapToAuthResponse(savedUser, token);
    }

    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        log.info("User logging in: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("fullName", user.getFullName());
        extraClaims.put("email", user.getEmail());

        String token = jwtUtils.generateToken(userDetails, extraClaims);

        log.info("User logged in successfully: {}", user.getUsername());

        return mapToAuthResponse(user, token);
    }

    /**
     * Get current user info
     */
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    /**
     * Map User to AuthResponse
     */
    private AuthResponse mapToAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment())
                .role(user.getRole())
                .token(token)
                .loginTime(LocalDateTime.now())
                .build();
    }

    /**
     * Map User to UserResponse
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
