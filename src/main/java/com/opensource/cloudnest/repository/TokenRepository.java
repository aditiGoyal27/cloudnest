package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token); // Add a method to retrieve a token by its token value>

}
