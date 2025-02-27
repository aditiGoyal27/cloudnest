package com.opensource.cloudnest.service;

import com.opensource.cloudnest.entity.Token;
import com.opensource.cloudnest.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public String createToken(String email) {
        // Generate a random token (you can also use JWT if needed)
        String token = UUID.randomUUID().toString();

        // Set token properties
        Token verificationToken = new Token();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours

        // Save to database
        tokenRepository.save(verificationToken);

        return token;
    }

    public Optional<Token> validateToken(String token) {
        Optional<Token> verificationToken = tokenRepository.findByToken(token);

        // Check if token exists and is not expired
        if (verificationToken.isPresent() &&
                verificationToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return verificationToken;
        }

        return Optional.empty();
    }
}
