package com.opensource.cloudnest.configuration;

import com.opensource.cloudnest.dto.response.PermissionResponse;
import com.opensource.cloudnest.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetId, Object permission) {
        if (authentication == null || permission == null || targetId == null) {
            return false;
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            String requiredPermission = (String) permission;
            String role = userDetails.getRole();

            if (role == null) {
                return false;
            }

            try {
                List<PermissionResponse> rolePermissions = permissionService.getAccessiblePermissionsByRole(role);
                return rolePermissions.stream()
                        .map(PermissionResponse::getName)
                        .anyMatch(requiredPermission::equals);
            } catch (Exception ex) {
                // Log the error for debugging
                System.err.println("Error fetching permissions: " + ex.getMessage());
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
