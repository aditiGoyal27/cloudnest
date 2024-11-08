package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.InviteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteUserRepository extends JpaRepository<InviteUser , Long> {
    Optional<InviteUser> findByEmail(String email);
    Optional<InviteUser> findByInviteToken(String inviteToken);
}
