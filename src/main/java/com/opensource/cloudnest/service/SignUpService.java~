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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SignUpService {
    private static final int OTP_LENGTH = 6;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailVerificationService emailVerificationService;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public ResDTO<Object> completeSignup(SignUpDTO signUpAdminDTO) {
        String email = signUpAdminDTO.getEmail();
        String contactNumber = signUpAdminDTO.getContactNumber();
        String password =  passwordEncoder.encode(signUpAdminDTO.getPassword());
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        if(optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            profile.setPassword(password);
            profile.setContactNumber(contactNumber);
            profileRepository.save(profile);
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Signed up successfully");
        }
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setPassword(password);
        profile.setContactNumber(contactNumber);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN.name());
        role.ifPresent(profile::setRole);
        SecureRandom random = new SecureRandom();
        profileRepository.save(profile);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Signed up successfully");

    }
}
