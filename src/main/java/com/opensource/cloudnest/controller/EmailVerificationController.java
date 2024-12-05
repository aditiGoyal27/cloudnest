package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.entity.EmailVerification;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.EmailVerificationRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/api/verify")
public class EmailVerificationController {

    @Autowired
    private EmailVerificationRepository repository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/email")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return repository.findByEmail(email)
                .map(verification -> {
                    // Check if OTP matches and is within 5 minutes of generation
                    if (verification.getOtp().equals(otp) &&
                            ChronoUnit.MINUTES.between(verification.getCreatedAt(), LocalDateTime.now()) <= 5) {

                        // Find and enable the user profile
                        Optional<Profile> optionalProfile = profileRepository.findByEmail(email); // Assuming email links to the profile
                        if (optionalProfile.isPresent()) {
                            Profile user = optionalProfile.get();
                            user.setEnabled(true); // Set enabled to true
                            profileRepository.save(user); // Save updated profile
                            return "OTP verified and profile enabled successfully";
                        } else {
                            return "Profile not found for the provided email";
                        }
                    } else {
                        return "Invalid or expired OTP";
                    }
                })
                .orElse("No OTP found for the provided email");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        if (optionalProfile.isPresent()) {
            // Generate a new OTP
            String newOtp = generateOtp(); // Implement this method to create a random OTP
            LocalDateTime createdAt = LocalDateTime.now();

            // Save or update the OTP in the repository
            Optional<EmailVerification> optionalEmailVerification = repository.findByEmail(email);
            if(optionalEmailVerification.isEmpty()) {
              return  ResponseEntity.ok("Email Id not found");
            }
            EmailVerification verification = optionalEmailVerification.get();
            verification.setEmail(email);
            verification.setOtp(newOtp);
            verification.setCreatedAt(createdAt);
            repository.save(verification);

            // Send the OTP to the user's email
            emailVerificationService.sendVerificationEmail(email, newOtp); // Implement `sendOtpEmail` in your EmailService

            return ResponseEntity.ok("OTP resent successfully to the provided email");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found for the provided email");
        }
    }

    private String generateOtp() {
        // Simple OTP generator (returns a 6-digit random number as a string)
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

}