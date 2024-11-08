package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.service.ProfileAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile/admin")
public class ProfileAdminController {

    @Autowired
    private ProfileAdminService profileAdminService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/create/{superAdminId}")
    public ResponseEntity<ResDTO<Object>> createAdmin(@RequestBody SignUpDTO signUpDTO , @PathVariable String superAdminId) {
        return new ResponseEntity<>(profileAdminService.signUpAdmin(signUpDTO , superAdminId), HttpStatus.OK);

    }

}
