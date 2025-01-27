package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.PermissionRequestDto;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.PermissionRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.PermissionService;
import com.opensource.cloudnest.service.ProfileAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://app.wavematrix.ai/"})
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProfileAdminService profileAdminService;
    @Autowired
    ProfileRepository profileRepository;
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasPermission(authentication.principal.id, 'CREATE_PERMISSION')" )
    @PostMapping("/addPermissionName")
    public ResponseEntity<ResDTO<Object>> addPermissionName(@RequestParam String name , @RequestParam String category , @RequestParam String description , HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Sorry token invalid"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.addPermissionName(name , category , description), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getPermissionName")
    public ResponseEntity<ResDTO<Object>> getPermissions(  HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND , "Sorry token invalid"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.getPermissions(), HttpStatus.OK);
    }
//
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN' ,'ADMIN') and hasPermission(authentication.principal.id, 'ADD_PERMISSION_TO_ROLE')" )
//    @PostMapping("/add")
//    public ResponseEntity<ResDTO<Object>> addPermissionToRole(@RequestBody PermissionDTO permissionDTO ,HttpServletRequest request) {
//        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
//            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Sorry token invalid"), HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>(permissionService.addPermissionAlpha(permissionDTO), HttpStatus.OK);
//    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasPermission(authentication.principal.id, 'VIEW_PERMISSION')" )
    @GetMapping("/getALLPermissionsToRole")
    public ResponseEntity<ResDTO<Object>> getPermissionToRole (HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND,"Sorry token invalid"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.getAllPermissionsWithRolesAlpha(), HttpStatus.OK);
    }
   /* @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/revokePermission/{superAdminId}/{roleName}")
    public ResponseEntity<ResDTO<Object>> revokePermission( @PathVariable Integer superAdminId, @PathVariable String roleName, HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, superAdminId), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.revokePermission(roleName), HttpStatus.OK);
    }*/




    @PreAuthorize("hasRole('SUPER_ADMIN') or hasPermission(authentication.principal.id, 'UPDATE_PERMISSION')" )
    @PostMapping("/add")
    public ResponseEntity<ResDTO<Object>> addPermissionToRole(@RequestBody List<PermissionRequestDto> permissionRequestDtos , HttpServletRequest request) {
        if (!JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResponseEntity<>(new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Sorry token invalid"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(permissionService.addPermissionAlpha(permissionRequestDtos), HttpStatus.OK);
    }

}
