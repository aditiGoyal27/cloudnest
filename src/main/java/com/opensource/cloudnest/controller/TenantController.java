package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.TenantDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/create/{superAdminId}")
    public ResDTO<Object> createTenant(HttpServletRequest request , @PathVariable Integer superAdminId , @RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,tenantService.createTenant(superAdminId , tenantDTO));
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
    @PostMapping("/update/{superAdminId}/{tenantId}")
    public ResDTO<Object> updateTenant(HttpServletRequest request ,@PathVariable Integer superAdminId ,@PathVariable Long tenantId,@RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.updateTenant(superAdminId ,tenantId,tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE ,ResDTOMessage.FAILURE, "Invalid Data");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{superAdminId}")
    public ResDTO<Object> deleteTenant(@RequestParam Long tenantId , HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.deleteTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/suspend/{superAdminId}")
    public ResDTO<Object> suspendTenant(@RequestParam Long tenantId , HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.suspendTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/reactivate/{superAdminId}")
    public ResDTO<Object> reactivateTenant(HttpServletRequest request ,@RequestParam Long tenantId , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.reactivateTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/superAdmin/dashboard/{superAdminId}")
    public ResDTO<Object> dashboard(HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboard());
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard/tenantAdmins")
    public ResDTO<Object> dashboardTenantAdmins(HttpServletRequest request ,@PathVariable Integer tenantAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, tenantAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboardTenantAdmins(tenantAdminId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getAllTenants")
    public ResDTO<Object> getAllTenants(HttpServletRequest request ,@RequestParam Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.getAllTenants());
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/getFilterTenants")
    public ResDTO<Object> getFilterTenants(HttpServletRequest request , @RequestParam Integer superAdminId ,@RequestParam String orgName , @RequestParam String orgAdminName) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
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
