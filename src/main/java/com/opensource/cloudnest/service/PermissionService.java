package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Permission;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.repository.PermissionRepository;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ResDTO<Object> addPermission(PermissionDTO permissionDTO) {
        String permissionName = permissionDTO.getPermissionName();
        List<String> role = permissionDTO.getRole();
        for(String roleName : role) {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            if(optionalRole.isPresent()) {
                Permission permission = new Permission();
                permission.setPermissionName(permissionName);
                permission.setRole(optionalRole.get());
                permissionRepository.save(permission);

            }
            else {
                return new ResDTO<>(Boolean.TRUE , ResDTOMessage.FAILURE , "Given Role does not exists" + roleName);
            }
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Permissions added Successfully");
    }

    public ResDTO<Object> revokePermission(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isPresent()) {
            Optional<Permission> optionalPermission = permissionRepository.findByRole(optionalRole.get());
            optionalPermission.ifPresent(permission -> permissionRepository.delete(permission));
        }
        else {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.RECORD_NOT_FOUND , "Permissions does not exists for the given role");
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Permissions revoked Successfully for role" + roleName);
    }

}
