package com.studyplatform.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.verification.base-url}")
    private String baseUrl;

    @Async
    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Study Platform - Email Verification");

        String verificationUrl = baseUrl + "/verification-success.html?token=" + token;
        String htmlContent = "<html><body>"
                + "<h2>Welcome to Study Platform!</h2>"
                + "<p>Please click the link below to verify your email address:</p>"
                + "<p><a href='" + verificationUrl + "'>Verify Email</a></p>"
                + "<p>This link will expire in 60 minutes.</p>"
                + "<p>If you did not create an account, please ignore this email.</p>"
                + "</body></html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
