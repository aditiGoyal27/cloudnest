package com.opensource.cloudnest.service;

import com.opensource.cloudnest.configuration.ServiceUrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ServiceUrlConfig serviceUrlConfig;

    public void sendResetLink(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link: " + resetLink);
        mailSender.send(message);
    }

    public void sendSignUpLink(String email, String token) {
        String signupUrl =serviceUrlConfig.getSignUpLinkUrl()+ "?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Complete Your Signup");
        message.setText("Click the link to complete your signup: " + signupUrl);

        mailSender.send(message);
    }
}
