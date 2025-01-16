package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.request.LoginRequest;
import com.opensource.cloudnest.dto.response.JwtResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PermissionService permissionService;

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
        if(!optionalProfile.get().isEnabled()) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.FAILURE, "Profile is inactive");
        }
        String token = jwtTokenProvider.generateToken(loginRequest.getEmail(), optionalProfile.get().getId() );

        JwtResponse jwtResponse = new JwtResponse(token);
        optionalProfile.ifPresent(profile -> jwtResponse.setProfileId(profile.getId()));
        optionalProfile.ifPresent(profile -> jwtResponse.setName(profile.getName()));
        optionalProfile.ifPresent(profile -> jwtResponse.setEmail(profile.getEmail()));
        optionalProfile.ifPresent(profile -> jwtResponse.setStatus(profile.getStatus()));
        optionalProfile.ifPresent(profile -> jwtResponse.setStatus(profile.getStatus()));
        optionalProfile.ifPresent(profile -> jwtResponse.setCreatedOn(profile.getCreatedOn()));
        optionalProfile.ifPresent(profile -> jwtResponse.setUpdatedOn(profile.getUpdatedOn()));
        optionalProfile.ifPresent(profile -> jwtResponse.setRoleId(profile.getRole().getId()));
        optionalProfile.ifPresent(profile -> jwtResponse.setRoleName(profile.getRole().getName()));
        optionalProfile.ifPresent(profile -> jwtResponse.setPermissionResponse(permissionService.getAccessiblePermissionsByRole(profile.getRole().getName())));
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, jwtResponse);

    }
}