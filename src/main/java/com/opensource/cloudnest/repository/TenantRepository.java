package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByOrgUnitName(String orgName);
    Optional<Tenant> findByOrgEmail(String orgEmail);
    Tenant findByAdmin(Profile Admin);
}
