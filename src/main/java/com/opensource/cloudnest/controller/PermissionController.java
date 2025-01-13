package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.PermissionRepository;
import com.opensource.cloudnest.service.PermissionService;
import com.opensource.cloudnest.service.ProfileAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProfileAdminService profileAdminService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN') and hasPermission(#Id, 'ADD_PERMISSION_NAME')" )
    @PostMapping("/addPermissionName/{Id}")
    public ResponseEntity<ResDTO<Object>> addPermissionName(@RequestParam String name , @PathVariable Integer Id, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, Id)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, Id), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.addPermissionName(name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN') and hasPermission(#Id, 'GET_PERMISSION_NAME')" )
    @GetMapping("/getPermissionName/{Id}")
    public ResponseEntity<ResDTO<Object>> getPermissions( @PathVariable Integer Id, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, Id)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, Id), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.getPermissions(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN' ,'ADMIN') and hasPermission(#Id, 'ADD_PERMISSION_TO_ROLE')" )
    @PostMapping("/add/{Id}")
    public ResponseEntity<ResDTO<Object>> addPermissionToRole(@RequestBody PermissionDTO permissionDTO , @PathVariable Integer Id, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, Id)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, Id), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.addPermission(permissionDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN') and hasPermission(#Id, 'GET_PERMISSION_TO_ROLE')" )
    @GetMapping("/getALLPermissionsToRole/{Id}")
    public ResponseEntity<ResDTO<Object>> getPermissionToRole (@PathVariable Integer Id, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, Id)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, Id), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.getAllPermissionsWithRoles(), HttpStatus.OK);
    }
   /* @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/revokePermission/{superAdminId}/{roleName}")
    public ResponseEntity<ResDTO<Object>> revokePermission( @PathVariable Integer superAdminId, @PathVariable String roleName, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, superAdminId), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.revokePermission(roleName), HttpStatus.OK);
    }*/


}
