package com.studyplatform.service;

import com.studyplatform.dto.request.LoginRequest;
import com.studyplatform.dto.request.RegisterRequest;
import com.studyplatform.model.User;
import com.studyplatform.repository.UserRepository;
import com.studyplatform.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;

    /**
     * Register a new user
     * @param request the registration request
     * @return a map containing user information and verification status
     */
    @Transactional
    public Map<String, Object> register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.STUDENT)
                .emailVerified(false) // User is not verified by default
                .build();

        // Save user to database
        user = userRepository.save(user);

        // Send verification email
        verificationTokenService.sendVerificationEmail(user);

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("emailVerificationRequired", true);
        response.put("message", "Registration successful. Please check your email to verify your account.");

        return response;
    }

    /**
     * Create a teacher or admin user (for administrative use only)
     * @param username the username
     * @param email the email
     * @param password the password
     * @param role the role (TEACHER or ADMIN)
     * @return the created user
     * @throws IllegalArgumentException if the role is STUDENT (students should use the register method)
     */
    @Transactional
    public User createStaffUser(String username, String email, String password, User.Role role) {
        // Only allow TEACHER or ADMIN roles
        if (role == User.Role.STUDENT) {
            throw new IllegalArgumentException("Students should use the register method");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .emailVerified(true) // Staff users are automatically verified
                .build();

        // Save user to database
        return userRepository.save(user);
    }

    /**
     * Authenticate a user
     * @param request the login request
     * @return a map containing the JWT token and user information
     */
    public Map<String, Object> authenticate(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get user from database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }
}
