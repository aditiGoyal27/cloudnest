package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Token;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.SignUpService;
import com.opensource.cloudnest.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SignUpService signUpService;

    @GetMapping("/verify-token")
    public ResDTO<Object> verifyTokenAndRedirect(@RequestParam("token") String token) {
        Optional<Token> validToken = tokenService.validateToken(token);

        if (validToken.isPresent()) {
            Optional<Profile> optionalProfile = profileRepository.findByEmail(validToken.get().getEmail());
            if (optionalProfile.isPresent()) {
                Profile profile = optionalProfile.get();
                profile.setStatus("ACTIVE");
                profile.setEnabled(true);
                profileRepository.save(profile);
            }

            // Return the redirection URL instead of redirecting
            String email = validToken.get().getEmail();
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Email verified successfully : " + email);
        } else {
            // Token is invalid or expired
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid or expired token");
        }
    }

    @PostMapping("/completeSignup")
    public ResponseEntity<ResDTO<Object>> completeSignup(HttpServletRequest request , @RequestBody SignUpDTO signUpDTO) {
            return new ResponseEntity<>(signUpService.completeSignup(signUpDTO), HttpStatus.OK);
        }
}
