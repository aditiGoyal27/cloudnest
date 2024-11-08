package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.TenantDTO;
import com.opensource.cloudnest.service.TenantService;
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/create/{adminId}")
    public ResponseEntity<ResDTO<Object>> createTenant(@PathVariable Long adminId ,@RequestBody TenantDTO tenantDTO) {
        return new ResponseEntity<>(tenantService.createTenant(adminId , tenantDTO), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/assignAdmin")
    public ResponseEntity<ResDTO<Object>> assignTenantAdmin(@RequestParam Long tenantId, @RequestParam Long adminId) {
        return new ResponseEntity<>(tenantService.assignTenantAdmin(tenantId , adminId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/assignUser")
    public ResponseEntity<ResDTO<Object>> assignUser(@RequestParam Long tenantId, @RequestParam Long userId) {
        return new ResponseEntity<>(tenantService.assignUser(tenantId , userId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/update/{adminId}")
    public ResponseEntity<ResDTO<Object>> updateTenant(@PathVariable Long adminId ,@RequestBody TenantDTO tenantDTO) {
        return new ResponseEntity<>(tenantService.updateTenant(adminId , tenantDTO), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{tenantId}")
    public ResponseEntity<ResDTO<Object>> deleteTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<>(tenantService.deleteTenant(tenantId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/suspend/{tenantId}")
    public ResponseEntity<ResDTO<Object>> suspendTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<>(tenantService.suspendTenant(tenantId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/reactivate/{tenantId}")
    public ResponseEntity<ResDTO<Object>> reactivateTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<>(tenantService.reactivateTenant(tenantId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<ResDTO<Object>> dashboard() {
        return new ResponseEntity<>(tenantService.dashboard(), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/dashboard/tenantAdmins/{tenantAdminId}")
    public ResponseEntity<ResDTO<Object>> dashboardTenantAdmins(@PathVariable Long tenantAdminId) {
        return new ResponseEntity<>(tenantService.dashboardTenantAdmins(tenantAdminId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/editTenant/settings/{tenantAdminId}")
    public ResponseEntity<ResDTO<Object>> editTenantSettings(@PathVariable Long tenantAdminId , @RequestBody TenantDTO tenantDTO) {
        return new ResponseEntity<>(tenantService.editTenantSettings(tenantAdminId , tenantDTO), HttpStatus.OK);

    }

}
