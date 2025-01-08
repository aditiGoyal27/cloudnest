package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PostMapping("/addRole")
    public ResponseEntity<ResDTO<Object>> createRole(HttpServletRequest request , @RequestParam Integer superAdminId ,  @RequestParam  String roleName , @RequestParam String description) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(roleService.createRole(roleName , description), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Role not added successfully"), HttpStatus.OK);
    }

    @GetMapping("/getRoles")
    public ResponseEntity<ResDTO<Object>> getRoles(HttpServletRequest request,@RequestParam Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Sorry unable to load"), HttpStatus.OK);
    }
}
