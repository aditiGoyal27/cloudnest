package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    ProfileRepository profileRepository;
    @PreAuthorize("hasAnyRole('SUPER_ADMIN' , 'ADMIN')")
    @PostMapping("/addRole")
    public ResponseEntity<ResDTO<Object>> createRole(HttpServletRequest request  ,  @RequestParam  String roleName , @RequestParam String description) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResponseEntity<>(roleService.createRole(roleName , description), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Role not added successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN' , 'ADMIN')")
    @GetMapping("/getRoles")
    public ResponseEntity<ResDTO<Object>> getRoles(HttpServletRequest request) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Sorry unable to load"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN' , 'ADMIN')")
    @GetMapping("/getFilteredRoles")
    public ResponseEntity<ResDTO<Object>> getFilteredRoles(HttpServletRequest request ,@RequestParam String roleName) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResponseEntity<>(roleService.getFilteredRoles(roleName), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Sorry unable to load"), HttpStatus.OK);
    }
}
