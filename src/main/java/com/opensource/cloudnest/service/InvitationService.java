package com.opensource.cloudnest.service;

import com.opensource.cloudnest.entity.InviteUser;
import com.opensource.cloudnest.repository.InviteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvitationService {

    @Autowired
    private InviteUserRepository inviteUserRepository;

    @Autowired
    private EmailVerificationService emailService;

    public String inviteUser(String email, String name) {
        InviteUser user = new InviteUser();
        user.setEmail(email);
        user.setName(name);
        user.setEnabled(false); // Account not enabled until registration
        String inviteToken = UUID.randomUUID().toString();
        user.setInviteToken(inviteToken);
        user.setInviteTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        inviteUserRepository.save(user);

        try {
            emailService.sendInviteEmail(email, inviteToken);
            return "Invitation sent successfully!";
        } catch (Exception e) {
            return "Failed to send invitation";
        }
    }

    public String completeRegistration(String token, String password) {
        return inviteUserRepository.findByInviteToken(token)
                .map(user -> {
                    if (user.getInviteTokenExpiry().isBefore(LocalDateTime.now())) {
                        return "Token expired";
                    }
                    user.setPassword(password); // Encrypt password in a real application
                    user.setEnabled(true);
                    user.setInviteToken(null);
                    inviteUserRepository.save(user);
                    return "Registration complete!";
                })
                .orElse("Invalid token");
    }
}
