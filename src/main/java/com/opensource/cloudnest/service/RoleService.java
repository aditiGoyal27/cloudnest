package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.RoleDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.dto.response.RoleResponse;
import com.opensource.cloudnest.dto.response.TenantResponse;
import com.opensource.cloudnest.entity.Permission;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.entity.Tenant;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProfileRepository profileRepository;
    public ResDTO<Object> createRole(RoleDTO roleDTO) {
        if(roleRepository.count()>8) {
            return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE,"Role cannot be added");
        }
        Role role = new Role();
        String roleName = roleDTO.getRoleName();
        role.setName(roleName);
        String description = roleDTO.getDescription();
        role.setDescription(description);
        role.setCreatedAt(LocalDateTime.now());
        roleRepository.save(role);
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,"Roles added successfully");
    }

    public ResDTO<Object> getRoles() {
        List<Role> roles = roleRepository.findAll(); // Get all roles>\
        List<RoleResponse> roleResponses = new ArrayList<>();

        for(Role role : roles) {
            long countUser = profileRepository.countByRole(role);
            RoleResponse roleResponse = new RoleResponse(role.getId(),role.getName(),role.getDescription() , countUser);
            roleResponses.add(roleResponse);
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,roleResponses);
    }
    public ResDTO<Object> getFilteredRoles(String roleName) {
        List<Role> roleList = roleRepository.findByNameContainingIgnoreCase(roleName);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, roleList);
    }

    public ResDTO<Object> addPermissionToRole(Role role, Permission permission){
        Optional<Role> roleInDatabase = roleRepository.findByName(role.getName());
        Role role1 = roleInDatabase.get();
        if(role1!=null){
            Set<Permission> permissionSet = role1.getPermissions();
            permissionSet.add(permission);
            role1.setPermissions(permissionSet);
            roleRepository.save(role1);
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,"Roles added successfully");

    }
    public ResDTO<Object> deletePermissionToRole(Role role, Permission permission){
        Optional<Role> roleInDatabase = roleRepository.findByName(role.getName());
        Role role1 = roleInDatabase.get();
        if(role1!=null){
            Set<Permission> permissionSet = role1.getPermissions();
            permissionSet.remove(permission);
            role1.setPermissions(permissionSet);
            roleRepository.save(role1);
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,"Roles added successfully");

    }
}
