package com.missinglink.backend.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String toEmail, String otp) {
        try {
            log.info("Sending from: {}", fromEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("MissingLink - Email Verification OTP");
            message.setText(
                    "Hello,\n\n" +
                            "Your OTP for email verification is: " + otp + "\n\n" +
                            "This OTP is valid for 10 minutes.\n\n" +
                            "If you did not register, please ignore this email.\n\n" +
                            "Team MissingLink"
            );
            mailSender.send(message);
            log.info("OTP sent successfully to {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send OTP to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }
}
