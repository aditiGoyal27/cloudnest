package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.*;
import com.opensource.cloudnest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserAccountService {

    private static final int OTP_LENGTH = 6;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private EmailVerificationService emailVerificationService;
    @Transactional
    public ResDTO<Object> createUser(SignUpDTO signUpUserDTO , Integer adminId) {
        String name = signUpUserDTO.getName();
        String userName = signUpUserDTO.getUserName();
        String email = signUpUserDTO.getEmail();
        String password =  passwordEncoder.encode(signUpUserDTO.getPassword());
        String contactNumber = signUpUserDTO.getContactNumber();
        String orgName = signUpUserDTO.getOrganizationName();
        Profile userAccount = new Profile();
        userAccount.setName(name);
        userAccount.setEmail(email);
        userAccount.setUserName(userName);
        userAccount.setPassword(password);
        userAccount.setContactNumber(contactNumber);
        userAccount.setStatus("ACTIVE");
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN.name());
        role.ifPresent(userAccount::setRole);
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findByOrgUnitName(orgName);
        profileRepository.save(userAccount);
        Relation relation = new Relation();
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        Optional<Profile> profile = profileRepository.findById(adminId);
        if(optionalProfile.isPresent()) {
            relation.setUserId(optionalProfile.get());
            relation.setAdminId(profile.get());
            relationRepository.save(relation);
        }
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Generates a digit between 0-9
        }
        emailVerificationService.sendVerificationEmail(email, otp.toString());
        EmailVerification verification = new EmailVerification();
        verification.setEmail(email);
        verification.setOtp(otp.toString());
        verification.setCreatedAt(LocalDateTime.now());
        emailVerificationRepository.save(verification);
        profileRepository.save(userAccount);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "User created successfully");
    }

    @Transactional
    public ResDTO<Object> updateUser(SignUpDTO signUpDTO , Integer adminId) {
        String name = signUpDTO.getName();
        String userName = signUpDTO.getUserName();
        String email = signUpDTO.getEmail();
        String password =  passwordEncoder.encode(signUpDTO.getPassword());
        String contactNumber = signUpDTO.getContactNumber();
        String orgName = signUpDTO.getOrganizationName();

        Optional<Profile> optionalUserAccount = profileRepository.findByEmail(email);
        if(optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Profile userAccount = optionalUserAccount.get();
        userAccount.setName(name);
        userAccount.setEmail(email);
        userAccount.setUserName(userName);
        userAccount.setPassword(password);
        userAccount.setContactNumber(contactNumber);
        userAccount.setStatus("ACTIVE");
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_USER.name());
        role.ifPresent(userAccount::setRole);
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findByOrgUnitName(orgName);
        profileRepository.save(userAccount);
        Relation relation = new Relation();
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        Optional<Profile> profile = profileRepository.findById(adminId);
        if(optionalProfile.isPresent()) {
            relation.setUserId(optionalProfile.get());
            relation.setAdminId(profile.get());
            relationRepository.save(relation);
        }
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "User Data Updated successfully");
    }


    @Transactional
    public ResDTO<Object> deleteUser(Integer adminId , Integer userId) {

        Optional<Profile> optionalUserAccount = profileRepository.findById(userId);

        if (optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "user data not found");
        }
        relationRepository.deleteByUserId(optionalUserAccount.get());
        profileRepository.delete(optionalUserAccount.get());
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.DELETED_SUCCESSFULLY, "user data deleted successfully");
    }

    @Transactional
    public ResDTO<Object> suspendUser(Integer adminId , Integer userId) {
        Optional<Profile> optionalUserAccount = profileRepository.findById(userId);
        if(optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Profile userAccount = optionalUserAccount.get();
        userAccount.setStatus("INACTIVE");
        profileRepository.save(userAccount);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "User Data suspended successfully");
    }


}

