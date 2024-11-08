package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.service.ProfileSuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/getProfileDetails")
    public ResponseEntity<ResDTO<Object>> getProfileDetails() {
        return new ResponseEntity<>(signUpAdminService.getProfileDetails(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/updateProfileDetails/{profileId}")
    public ResponseEntity<ResDTO<Object>> updateProfileDetails(@PathVariable Long profileId ,@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(signUpAdminService.updateProfileDetails(profileId,signUpDTO), HttpStatus.OK);
    }
}
