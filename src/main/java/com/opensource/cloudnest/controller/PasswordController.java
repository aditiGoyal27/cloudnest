package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.ServiceUrlConfig;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.request.NewPasswordRequest;
import com.opensource.cloudnest.dto.request.PasswordResetRequest;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.PasswordResetToken;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.PasswordResetTokenRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ServiceUrlConfig serviceUrlConfig;

    @PostMapping("/reset-password-request")
    public ResDTO<Object> requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            Optional<Profile> profile = profileRepository.findByEmail(passwordResetRequest.getEmail());
            if (profile.isEmpty()) {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "user data not found");
            }

            String token = createPasswordResetToken(profile.get());
            String resetLink = serviceUrlConfig.getResetLinkUrl() +"?token=" + token;

            emailService.sendResetLink(profile.get().getEmail(), resetLink);
        }catch (Exception e) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, e.getMessage());
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.SUCCESS, "Password Link Sent to the user email");
    }

    public String createPasswordResetToken(Profile user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setProfile(user);
        resetToken.setExpirationTime(LocalDateTime.now().plusMinutes(30)); // 30 min expiry
        tokenRepository.save(resetToken);
        return token;
    }

    @PostMapping("/reset-password")
    public ResDTO<Object> resetPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest newPasswordRequest) {
        try {
            Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
            if(resetToken.isEmpty()) {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Password reset token expired or does not exist");
            }

            if (resetToken.get().getExpirationTime().isBefore(LocalDateTime.now())) {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Password reset token expired or does not exist");
            }
            Profile profile = resetToken.get().getProfile();
            profile.setPassword(new BCryptPasswordEncoder().encode(newPasswordRequest.getNewPassword())); // Hash password
            profileRepository.save(profile);
            tokenRepository.delete(resetToken.get()); // Remove used token
        } catch (Exception e) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, e.getMessage());
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.SUCCESS, "Password reset successfully");    }

}
