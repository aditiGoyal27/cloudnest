package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification , Long> {
    Optional<EmailVerification> findByEmail(String email);
}
