package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
Optional<Profile> findByEmail(String email);
Optional<Profile> findByUserName(String userName);


}
