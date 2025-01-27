package com.opensource.cloudnest.repository;


import com.opensource.cloudnest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findByNameContainingIgnoreCase(String roleName);
    List<Role> findByNameInIgnoreCase(List<String> roleName);


    boolean existsByName(String roleName);
}
