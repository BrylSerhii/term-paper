package com.studyplatform.controller;

import com.studyplatform.model.User;
import com.studyplatform.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;

    /**
     * Create a new teacher or admin user
     * @param request the request containing user details
     * @return a response with the created user information
     */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createStaffUser(@RequestBody CreateStaffUserRequest request) {
        try {
            User user = authenticationService.createStaffUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getRole()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("message", "User created successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", true,
                    "message", e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Request class for creating a staff user
     */
    public static class CreateStaffUserRequest {
        private String username;
        private String email;
        private String password;
        private User.Role role;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public User.Role getRole() {
            return role;
        }

        public void setRole(User.Role role) {
            this.role = role;
        }
    }
}