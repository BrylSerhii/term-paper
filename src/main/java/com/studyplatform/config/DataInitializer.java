package com.studyplatform.config;

import com.studyplatform.model.User;
import com.studyplatform.repository.UserRepository;
import com.studyplatform.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    /**
     * Initialize default admin user if no admin exists
     * Only runs in development environment
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner initializeData() {
        return args -> {
            // Check if admin user exists
            boolean adminExists = userRepository.findAll().stream()
                    .anyMatch(user -> user.getRole() == User.Role.ADMIN);

            // Create admin user if none exists
            if (!adminExists) {
                try {
                    User admin = authenticationService.createStaffUser(
                            "admin",
                            "admin@studyplatform.com",
                            "admin123", // This should be changed in production
                            User.Role.ADMIN
                    );
                    System.out.println("Created default admin user: " + admin.getUsername());
                } catch (Exception e) {
                    System.err.println("Failed to create default admin user: " + e.getMessage());
                }
            }

            // Create a default teacher if no teacher exists
            boolean teacherExists = userRepository.findAll().stream()
                    .anyMatch(user -> user.getRole() == User.Role.TEACHER);

            if (!teacherExists) {
                try {
                    User teacher = authenticationService.createStaffUser(
                            "teacher",
                            "teacher@studyplatform.com",
                            "teacher123", // This should be changed in production
                            User.Role.TEACHER
                    );
                    System.out.println("Created default teacher user: " + teacher.getUsername());
                } catch (Exception e) {
                    System.err.println("Failed to create default teacher user: " + e.getMessage());
                }
            }
        };
    }
}