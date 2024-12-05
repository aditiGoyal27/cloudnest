package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Permission;
import com.opensource.cloudnest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission , Long> {
    Optional<Permission> findByRole(Role role);
}
