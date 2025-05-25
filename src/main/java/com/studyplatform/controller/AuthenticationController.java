package com.studyplatform.controller;

import com.studyplatform.dto.request.LoginRequest;
import com.studyplatform.dto.request.RegisterRequest;
import com.studyplatform.model.User;
import com.studyplatform.service.AuthenticationService;
import com.studyplatform.service.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final VerificationTokenService verificationTokenService;

    /**
     * Register a new user
     * @param request the registration request
     * @return a response with the JWT token and user information
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            Map<String, Object> response = authenticationService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", true,
                    "message", e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();

            // Return a generic error message
            Map<String, Object> errorResponse = Map.of(
                    "error", true,
                    "message", "An error occurred during registration. Please try again."
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Authenticate a user
     * @param request the login request
     * @return a response with the JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", true,
                    "message", "Invalid username or password"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Verify a user's email address
     * @param token the verification token
     * @return a response indicating whether the verification was successful
     */
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        boolean verified = verificationTokenService.verifyToken(token);

        if (verified) {
            Map<String, Object> response = Map.of(
                    "verified", true,
                    "message", "Email verification successful. You can now log in."
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = Map.of(
                    "verified", false,
                    "error", true,
                    "message", "Invalid or expired verification token."
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Validate the JWT token and return user information
     * @return a response with the user information if the token is valid
     */
    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken() {
        // Get the current authentication from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            // Create response with user information
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } else {
            // Return error response if not authenticated
            Map<String, Object> errorResponse = Map.of(
                    "valid", false,
                    "error", true,
                    "message", "Invalid or expired token."
            );
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
