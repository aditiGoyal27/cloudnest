package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<Profile, Long> {

}
