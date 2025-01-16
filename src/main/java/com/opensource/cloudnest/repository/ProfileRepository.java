package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
Optional<Profile> findByEmail(String email);
Optional<Profile> findByTenant(Tenant tenant);
List<Profile> findByAdmin(Profile admin);
Optional<Profile> findByRole(Role role);
long countByRole(Role role);
}
