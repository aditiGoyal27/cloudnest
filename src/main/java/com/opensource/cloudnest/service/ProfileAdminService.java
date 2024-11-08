package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.EmailVerification;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.entity.RoleEnum;
import com.opensource.cloudnest.repository.EmailVerificationRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.repository.RoleRepository;
import com.opensource.cloudnest.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfileAdminService {
    private static final int OTP_LENGTH = 6;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Transactional
    public ResDTO<Object> signUpAdmin(SignUpDTO signUpAdminDTO, String superAdminId) {
        try {
            // Extracting data from DTO
            String name = signUpAdminDTO.getName();
            String userName = signUpAdminDTO.getUserName();
            String email = signUpAdminDTO.getEmail();
            String password = passwordEncoder.encode(signUpAdminDTO.getPassword());
            String contactNumber = signUpAdminDTO.getContactNumber();
            String orgName = signUpAdminDTO.getOrganizationName();

            // Create and populate profile
            Profile profile = new Profile();
            profile.setName(name);
            profile.setEmail(email);
            profile.setUserName(userName);
            profile.setPassword(password);
            profile.setContactNumber(contactNumber);

            // Set role
            Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
            if (role.isPresent()) {
                profile.setRole(role.get());
            } else {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Admin role not found");
            }

            // Save profile
            profileRepository.save(profile);

            // Generate OTP
            String otp = generateOtp();
            emailVerificationService.sendVerificationEmail(email, otp);

            // Save OTP to the repository
            EmailVerification verification = new EmailVerification();
            verification.setEmail(email);
            verification.setOtp(otp);
            verification.setCreatedAt(LocalDateTime.now());
            emailVerificationRepository.save(verification);

            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Admin created successfully");

        } catch (Exception e) {

            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "An error occurred during signup. Please try again later.");
        }
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Generates a digit between 0-9
        }
        return otp.toString();
    }

}
