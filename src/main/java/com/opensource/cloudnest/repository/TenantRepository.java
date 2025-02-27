package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByTenantAdmin(Profile Admin);
    Optional<Tenant> findByTenantName(String tenantName);
 }
