package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.request.LoginRequest;
import com.opensource.cloudnest.dto.response.JwtResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ProfileRepository profileRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResDTO<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        Optional<Profile> optionalProfile = profileRepository.findByEmail(loginRequest.getEmail());
        if(optionalProfile.isEmpty()) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.FAILURE, "Profile does not exist");
        }
        String token = jwtTokenProvider.generateToken(loginRequest.getEmail(), optionalProfile.get().getId() );

        JwtResponse jwtResponse = new JwtResponse(token);
        optionalProfile.ifPresent(profile -> jwtResponse.setProfileId(profile.getId()));
        optionalProfile.ifPresent(profile -> jwtResponse.setName(profile.getName()));
        optionalProfile.ifPresent(profile -> jwtResponse.setEmail(profile.getEmail()));
        optionalProfile.ifPresent(profile -> jwtResponse.setStatus(profile.getStatus()));
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, jwtResponse);

    }
}