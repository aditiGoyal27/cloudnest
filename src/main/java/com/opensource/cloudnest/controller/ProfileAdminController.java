package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.ProfileAdminService;
import com.opensource.cloudnest.service.ProfileSuperAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/profile/admin")
public class ProfileAdminController {

    @Autowired
    private ProfileAdminService profileAdminService;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @PreAuthorize("hasRole('SUPER_ADMIN') and hasPermission(#superAdminId, 'CREATE_ADMIN')")
    @PostMapping("/create/{tenantId}")
    public ResDTO<Object> createAdmin(@RequestBody SignUpDTO signUpDTO , @PathVariable Long tenantId , HttpServletRequest request) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request ,profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS , profileAdminService.signUpAdmin(signUpDTO, jwtTokenProvider.getProfileFromRequest(request) ,tenantId ));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "user data not found");
    }

}
