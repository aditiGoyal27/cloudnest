package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.ProfileSuperAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/profile/superAdmin")
public class ProfileSuperAdminController {

    @Autowired
    private ProfileSuperAdminService signUpAdminService;

    @PostMapping("/create")
    public ResponseEntity<ResDTO<Object>> createSuperAdmin(@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(signUpAdminService.signUpSuperAdmin(signUpDTO), HttpStatus.OK);

    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getProfileDetails/{superAdminId}")
    public ResDTO<Object> getProfileDetails(HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.FALSE , ResDTOMessage.SUCCESS ,signUpAdminService.getProfileDetails());
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "user data not found");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/updateProfileDetails/{profileId}")
    public ResponseEntity<ResDTO<Object>> updateProfileDetails(@PathVariable Integer profileId ,@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(signUpAdminService.updateProfileDetails(profileId,signUpDTO), HttpStatus.OK);
    }
}
