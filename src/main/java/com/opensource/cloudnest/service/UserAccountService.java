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
import java.util.List;
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
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Transactional
    public ResDTO<Object> createUser(SignUpDTO signUpUserDTO , Integer adminId ,Long tenantId) {
        String name = signUpUserDTO.getName();
        String email = signUpUserDTO.getEmail();
        String contactNumber = signUpUserDTO.getContactNumber();
        String assignedRole = signUpUserDTO.getAssignedRole();
        Profile userAccount = new Profile();
        userAccount.setName(name);
        userAccount.setEmail(email);
        userAccount.setContactNumber(contactNumber);
        userAccount.setAssignedRole(assignedRole);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_USER.name());
        role.ifPresent(userAccount::setRole);
        Optional<Profile> optionalProfile = profileRepository.findById(adminId);
        optionalProfile.ifPresent(userAccount::setAdmin);
        profileRepository.save(userAccount);
        Relation relation = new Relation();
        Optional<Profile> optionalProfile1 = profileRepository.findByEmail(email);
        Optional<Profile> profile = profileRepository.findById(adminId);
        if(optionalProfile1.isPresent() && profile.isPresent()) {
            relation.setUserId(optionalProfile1.get());
            relation.setAdminId(profile.get());
            relationRepository.save(relation);
        }
        String token = tokenService.createToken(email);
        emailService.sendSignUpLink(email, token);
        Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
        optionalTenant.ifPresent(userAccount::setTenant);
        profileRepository.save(userAccount);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "User created successfully");
    }

    @Transactional
    public ResDTO<Object> updateUser(SignUpDTO signUpDTO , Integer adminId) {
        String name = signUpDTO.getName();
        String email = signUpDTO.getEmail();
        String password =  passwordEncoder.encode(signUpDTO.getPassword());
        String contactNumber = signUpDTO.getContactNumber();

        Optional<Profile> optionalUserAccount = profileRepository.findByEmail(email);
        if(optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Profile userAccount = optionalUserAccount.get();
        userAccount.setName(name);
        userAccount.setEmail(email);
        userAccount.setPassword(password);
        userAccount.setStatus("ACTIVE");
        userAccount.setContactNumber(contactNumber);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_USER.name());
        role.ifPresent(userAccount::setRole);
        userAccount.setAssignedRole(signUpDTO.getAssignedRole());
        profileRepository.save(userAccount);
        Relation relation = new Relation();
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        Optional<Profile> profile = profileRepository.findById(adminId);
        if(optionalProfile.isPresent()) {
            relation.setUserId(optionalProfile.get());
            relation.setAdminId(profile.get());
            relationRepository.save(relation);
        }

        String token = tokenService.createToken(email);
        emailService.sendSignUpLink(email, token);

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "User Data Updated successfully");
    }


    @Transactional
    public ResDTO<Object> deleteUser(Integer adminId , Integer userId) {

        Optional<Profile> optionalUserAccount = profileRepository.findById(userId);

        if (optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "user data not found");
        }

        optionalUserAccount.get().setEnabled(false);
        profileRepository.save(optionalUserAccount.get());
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
        userAccount.setEnabled(false);
        profileRepository.save(userAccount);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "User Data suspended successfully");
    }



    @Transactional
    public ResDTO<Object> reactivateUser(Integer adminId , Integer userId) {
        Optional<Profile> optionalUserAccount = profileRepository.findById(userId);
        if(optionalUserAccount.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Profile userAccount = optionalUserAccount.get();
        userAccount.setStatus("ACTIVE");
        userAccount.setEnabled(true);
        profileRepository.save(userAccount);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "User Reactivated successfully");
    }
}

