package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public ResDTO<Object> createRole(String roleName , String description) {
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
}
