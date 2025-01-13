package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.PermissionResponse;
import com.opensource.cloudnest.dto.response.PermissionWithRolesResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Permission;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.repository.PermissionRepository;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ResDTO<Object> addPermissionName(String permissionName) {
                Permission permission = new Permission();
                permission.setName(permissionName);
                permission.setDescription("");
                permission.setCreatedAt(LocalDateTime.now());
                permissionRepository.save(permission);
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Permissions added Successfully");
    }


    public ResDTO<Object> getPermissions() {
       List<Permission> permissions = permissionRepository.findAll(); // Get all permissions>
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS ,permissions);
    }

    public ResDTO<Object> addPermission(PermissionDTO permissionDTO) {
        Map<Long, List<Long>> rolePermissionsMap = permissionDTO.getRolePermissionsMap();
        // Assuming `PermissionDTO` contains a map where the key is the roleId and the value is a list of permissionIds

        for (Map.Entry<Long, List<Long>> entry : rolePermissionsMap.entrySet()) {
            Long roleId = entry.getKey();
            List<Long> permissionIds = entry.getValue();

            // Fetch the Role entity
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (roleOpt.isPresent()) {
                Role role = roleOpt.get();

                // Clear existing permissions
                role.getPermissions().clear();

                // Fetch and add new permissions for this role
                for (Long permissionId : permissionIds) {
                    Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);

                    if (permissionOpt.isPresent()) {
                        Permission permission = permissionOpt.get();
                        role.getPermissions().add(permission); // Assign the permission
                    } else {
                        throw new RuntimeException("Permission with ID " + permissionId + " not found");
                    }
                }

                // Save the updated role with the new permissions
                roleRepository.save(role);
            } else {
                // Handle the case where the role doesn't exist
                throw new RuntimeException("Role with ID " + roleId + " not found");
            }
        }

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Permissions added/updated successfully");
    }

    /*public ResDTO<Object> revokePermission(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isPresent()) {
            Optional<Permission> optionalPermission = permissionRepository.findByRole(optionalRole.get());
            optionalPermission.ifPresent(permission -> permissionRepository.delete(permission));
        }
        else {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.RECORD_NOT_FOUND , "Permissions does not exists for the given role");
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Permissions revoked Successfully for role" + roleName);
    }*/

    public List<PermissionResponse> getAccessiblePermissionsByRole(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isPresent()) {
            Set<Permission> permissions = optionalRole.get().getPermissions(); // Get the permissions associated with the role>
            return createPermissionResponse(permissions);
        }
        return null;
    }

    private List<PermissionResponse> createPermissionResponse(Set<Permission> permissions) {
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setName(permission.getName());
            permissionResponse.setId(permission.getId());
            permissionResponses.add(permissionResponse);
        }
        return permissionResponses;
    }

    public ResDTO<Object> getAllPermissionsWithRoles() {
        // Fetch all roles and permissions from the database
        List<Role> allRoles = roleRepository.findAll();
        List<Permission> allPermissions = permissionRepository.findAll();

        // Create a list for the response
        List<Map<String, Object>> permissionsResponse = new ArrayList<>();

        // Iterate over all permissions
        for (Permission permission : allPermissions) {
            // Find roles associated with this permission
            List<Map<String, Object>> associatedRoles = allRoles.stream()
                    .filter(role -> role.getPermissions().contains(permission)) // Check if the role has this permission
                    .map(role -> {
                        Map<String, Object> roleData = new HashMap<>();
                        roleData.put("id", role.getId());
                        roleData.put("name", role.getName());
                        return roleData;
                    })
                    .collect(Collectors.toList());

            // Build the permission response with associated roles
            Map<String, Object> permissionData = new HashMap<>();
            permissionData.put("PermissionId", permission.getId());
            permissionData.put("PermissionName", permission.getName());
            permissionData.put("roles", associatedRoles);

            permissionsResponse.add(permissionData);
        }

        // Create the final JSON structure
        Map<String, Object> response = new HashMap<>();
        response.put("permissions", permissionsResponse);

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, response);
    }
}
