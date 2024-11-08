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
import java.util.List;
import java.util.Optional;

@Service
public class ProfileSuperAdminService {

    private static final int OTP_LENGTH = 6;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public ResDTO<Object> signUpSuperAdmin(SignUpDTO signUpAdminDTO) {
      String name = signUpAdminDTO.getName();
      String userName = signUpAdminDTO.getUserName();
      String email = signUpAdminDTO.getEmail();
      String password =  passwordEncoder.encode(signUpAdminDTO.getPassword());
      String contactNumber = signUpAdminDTO.getContactNumber();
      Profile superAdmin = new Profile();
      superAdmin.setName(name);
      superAdmin.setEmail(email);
      superAdmin.setUserName(userName);
      superAdmin.setPassword(password);
      superAdmin.setContactNumber(contactNumber);
      superAdmin.setStatus("ACTIVE");
      Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN.name());
      role.ifPresent(superAdmin::setRole);
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Generates a digit between 0-9
        }
        emailVerificationService.sendVerificationEmail(email, otp.toString());
        profileRepository.save(superAdmin);
      emailVerificationService.sendVerificationEmail(email, otp.toString());
        EmailVerification verification = new EmailVerification();
        verification.setEmail(email);
        verification.setOtp(otp.toString());
        verification.setCreatedAt(LocalDateTime.now());
        emailVerificationRepository.save(verification);
      return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Super Admin created successfully");
    }

    public ResDTO<Object> getProfileDetails() {
        List<Profile> profileList = profileRepository.findAll();
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, profileList);
    }

    @Transactional
    public ResDTO<Object> updateProfileDetails(Long profileId ,SignUpDTO signUpAdminDTO) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if(optionalProfile.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "ProfileId not found");
        }

        Profile profile = optionalProfile.get();
        String name = signUpAdminDTO.getName();
        String userName = signUpAdminDTO.getUserName();
        String email = signUpAdminDTO.getEmail();
        String password =  passwordEncoder.encode(signUpAdminDTO.getPassword());
        String contactNumber = signUpAdminDTO.getContactNumber();

        profile.setName(name);
        profile.setEmail(email);
        profile.setUserName(userName);
        profile.setPassword(password);
        profile.setContactNumber(contactNumber);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN.name());
        role.ifPresent(profile::setRole);
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Generates a digit between 0-9
        }

        profileRepository.save(profile);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "ProfileId data updated successfully");
    }
}
