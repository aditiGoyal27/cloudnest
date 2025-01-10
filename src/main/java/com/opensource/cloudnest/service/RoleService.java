package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.dto.response.TenantResponse;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.entity.Tenant;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public ResDTO<Object> createRole(String roleName , String description) {
        if(roleRepository.count()>8) {
            return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE,"Role cannot be added");
        }
        Role role = new Role();
        role.setName(roleName);
        role.setDescription(description);
        role.setCreatedAt(LocalDateTime.now());
        roleRepository.save(role);
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,"Roles added successfully");
    }

    public ResDTO<Object> getRoles() {
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS,roleRepository.findAll());
    }
    public ResDTO<Object> getFilteredRoles(String roleName) {
        List<Role> roleList = roleRepository.findByNameContainingIgnoreCase(roleName);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, roleList);
    }
}
