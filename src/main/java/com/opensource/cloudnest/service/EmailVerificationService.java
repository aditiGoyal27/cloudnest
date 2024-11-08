package com.opensource.cloudnest.service;

import com.opensource.cloudnest.repository.ProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JavaMailSender mailSender;
    public void sendVerificationEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    public void sendInviteEmail(String email, String inviteToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String inviteUrl = "http://yourapp.com/register?token=" + inviteToken;
        String subject = "You are invited!";
        String content = "Click the link to join: " + inviteUrl;

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}

