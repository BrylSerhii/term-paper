package com.studyplatform.config;

import com.studyplatform.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application
 * This class configures Spring Security to allow access to static resources and the authentication endpoints
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Configure the security filter chain
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow access to static resources
                .requestMatchers("/", "/index.html", "/verification-success.html", "/css/**", "/js/**", "/images/**").permitAll()
                // Protect dashboard page
                .requestMatchers("/dashboard.html").authenticated()
                // Allow access to API endpoints
                .requestMatchers("/api/health", "/api/auth/login", "/api/auth/register", "/api/auth/verify").permitAll()
                // Allow access to token validation endpoint (JWT filter will handle authentication)
                .requestMatchers("/api/auth/validate-token").permitAll()
                // Restrict content management endpoints to teachers and admins
                .requestMatchers("/api/materials/**", "/api/assignments/**").hasAnyRole("TEACHER", "ADMIN")
                // Allow students to view courses and submit assignments
                .requestMatchers("/api/courses/**", "/api/enrollments/**", "/api/progress/**").authenticated()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in this example
            .formLogin(form -> form.disable()) // Disable form login to prevent default login page
            .httpBasic(basic -> basic.disable()); // Disable HTTP Basic authentication

        return http.build();
    }

    /**
     * Configure the authentication provider
     * @return the configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configure the password encoder
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure the authentication manager
     * @param config the AuthenticationConfiguration
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
