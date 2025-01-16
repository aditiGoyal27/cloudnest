package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.TenantDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    ProfileRepository profileRepository;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/create")
    public ResDTO<Object> createTenant(HttpServletRequest request  , @RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,tenantService.createTenant(jwtTokenProvider.getProfileFromRequest(request) , tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }



    /*@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignUser/{tenanadminId}")
    public ResDTO<Object> assignUser(HttpServletRequest request ,@RequestParam Long tenantId, @RequestParam Integer userId , @PathVariable Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.assignUser(tenantId , userId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }   */

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/update/{tenantId}")
    public ResDTO<Object> updateTenant(HttpServletRequest request  ,@PathVariable Long tenantId,@RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request ,  profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.updateTenant(jwtTokenProvider.getProfileFromRequest(request) ,tenantId,tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE ,ResDTOMessage.FAILURE, "Invalid Data");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete")
    public ResDTO<Object> deleteTenant(@RequestParam Long tenantId , HttpServletRequest request) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.deleteTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/suspend")
    public ResDTO<Object> suspendTenant(@RequestParam Long tenantId , HttpServletRequest request ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.suspendTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/reactivate")
    public ResDTO<Object> reactivateTenant(HttpServletRequest request ,@RequestParam Long tenantId ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request,    profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.reactivateTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/superAdmin/dashboard")
    public ResDTO<Object> dashboard(HttpServletRequest request ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboard());
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard/tenantAdmins")
    public ResDTO<Object> dashboardTenantAdmins(HttpServletRequest request ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboardTenantAdmins(jwtTokenProvider.getProfileFromRequest(request)));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getAllTenants")
    public ResDTO<Object> getAllTenants(HttpServletRequest request) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.getAllTenants());
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getFilterTenants")
    public ResDTO<Object> getFilterTenants(HttpServletRequest request ,@RequestParam String orgName , @RequestParam String orgAdminName) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request,    profileRepository)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.getFilterTenants(orgName , orgAdminName));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }
/*

    @PreAuthorize("hasAnyRole('ADMIN') and hasPermission(#tenantAdminId, 'EDIT_TENANT_SETTINGS')")
    @GetMapping("/editTenant/settings/{tenantAdminId}")
    public ResDTO<Object> editTenantSettings(HttpServletRequest request ,@PathVariable Integer tenantAdminId , @RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, tenantAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.editTenantSettings(tenantAdminId , tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }
*/

}
