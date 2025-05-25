package com.studyplatform.service;

import com.studyplatform.model.User;
import com.studyplatform.model.VerificationToken;
import com.studyplatform.repository.UserRepository;
import com.studyplatform.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.verification.expiration-minutes}")
    private int expirationMinutes;

    @Transactional
    public VerificationToken createVerificationToken(User user) {
        // Invalidate any existing tokens for this user
        tokenRepository.findByUser(user).forEach(token -> {
            token.setUsed(true);
            tokenRepository.save(token);
        });

        // Create a new token
        VerificationToken token = VerificationToken.generateToken(user, expirationMinutes);
        return tokenRepository.save(token);
    }

    @Transactional
    public boolean verifyToken(String token) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        
        if (verificationToken.isEmpty()) {
            return false;
        }
        
        VerificationToken vToken = verificationToken.get();
        
        // Check if token is already used or expired
        if (vToken.isUsed() || vToken.isExpired()) {
            return false;
        }
        
        // Mark token as used
        vToken.setUsed(true);
        tokenRepository.save(vToken);
        
        // Verify user's email
        User user = vToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        
        return true;
    }

    @Transactional
    public void sendVerificationEmail(User user) {
        try {
            VerificationToken token = createVerificationToken(user);
            emailService.sendVerificationEmail(user.getEmail(), token.getToken());
        } catch (Exception e) {
            // Log the error but don't throw it to avoid breaking the registration process
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }
}