package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.PermissionService;
import com.opensource.cloudnest.service.ProfileAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProfileAdminService profileAdminService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/add/{superAdminId}")
    public ResponseEntity<ResDTO<Object>> addPermission(@RequestBody PermissionDTO permissionDTO , @PathVariable Integer superAdminId, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, superAdminId), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.addPermission(permissionDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/revokePermission/{superAdminId}/{roleName}")
    public ResponseEntity<ResDTO<Object>> revokePermission( @PathVariable Integer superAdminId, @PathVariable String roleName, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, superAdminId), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.revokePermission(roleName), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
//    @PostMapping("/updatePermission/{roleId}/{permissionId}")
//    public ResponseEntity<ResDTO<Object>> updatePermission( @PathVariable Integer superAdminId, @PathVariable Long roleId,@PathVariable Long permissionId, HttpServletRequest request) {
//        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
//            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, superAdminId), HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>(permissionService.updatePermission(roleId , permissionId), HttpStatus.OK);
//    }
}
