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
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/create/{superAdminId}")
    public ResDTO<Object> createTenant(HttpServletRequest request , @PathVariable Integer superAdminId , @RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,tenantService.createTenant(superAdminId , tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/assignAdmin")
    public ResDTO<Object> assignTenantAdmin(HttpServletRequest request ,@RequestParam Long tenantId, @RequestParam Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.assignTenantAdmin(tenantId , adminId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/assignUser/{adminId}")
    public ResDTO<Object> assignUser(HttpServletRequest request ,@RequestParam Long tenantId, @RequestParam Integer userId , @PathVariable Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.assignUser(tenantId , userId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/update/{adminId}")
    public ResDTO<Object> updateTenant(HttpServletRequest request ,@PathVariable Integer adminId ,@RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.updateTenant(tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE ,ResDTOMessage.FAILURE, "Invalid Data");
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{superAdminId}")
    public ResDTO<Object> deleteTenant(@RequestParam Long tenantId , HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.deleteTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/suspend/{superAdminId}")
    public ResDTO<Object> suspendTenant(@RequestParam Long tenantId , HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.suspendTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/reactivate/{superAdminId}")
    public ResDTO<Object> reactivateTenant(HttpServletRequest request ,@RequestParam Long tenantId , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.reactivateTenant(tenantId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/dashboard/{superAdminId}")
    public ResDTO<Object> dashboard(HttpServletRequest request , @PathVariable Integer superAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, superAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboard());
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/dashboard/tenantAdmins")
    public ResDTO<Object> dashboardTenantAdmins(HttpServletRequest request ,@RequestParam Integer tenantAdminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, tenantAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.dashboardTenantAdmins(tenantAdminId));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/editTenant/settings/{tenantAdminId}")
    public ResDTO<Object> editTenantSettings(HttpServletRequest request ,@PathVariable Integer tenantAdminId , @RequestBody TenantDTO tenantDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, tenantAdminId)) {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,tenantService.editTenantSettings(tenantAdminId , tenantDTO));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

}
