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

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ResDTO<Object> addPermissionName(String permissionName) {
                Permission permission = new Permission();
                permission.setPermissionName(permissionName);
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
            permissionResponse.setName(permission.getPermissionName());
            permissionResponse.setId(permission.getId());
            permissionResponses.add(permissionResponse);
        }
        return permissionResponses;
    }

    public ResDTO<Object> getAllPermissionsWithRoles() {
        List<Role> allRoles = roleRepository.findAll();
        List<PermissionWithRolesResponse> responseList = new ArrayList<>();

        for (Role role : allRoles) {
            //Optional<Permission> permissionOpt = role.getPermissions().stream().findFirst();
            Set<Permission> permissions = role.getPermissions(); // assuming that the Role entity has a relationship with Permission>

            for (Permission permission : permissions) {
                PermissionWithRolesResponse response = new PermissionWithRolesResponse();
                PermissionResponse permissionResponse = new PermissionResponse();
                permissionResponse.setName(permission.getPermissionName());
                permissionResponse.setId(permission.getId());
                response.setPermissionResponse(permissionResponse);
                response.setRoleNames(role.getName());
                response.setRoleId(role.getId());
                responseList.add(response);
            }
            // Get the roles associated with this permission


        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , responseList);
    }




}
