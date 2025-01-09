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



@CrossOrigin(origins = { "http://mdmdev.elements91.in", "http://10.10.2.12:3000", "http://10.10.2.21:3000", "http://localhost:3000", "https://mdmdev.elements91.in"})
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
    public ResponseEntity<Object> verifyTokenAndRedirect(@RequestParam("token") String token) {
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
            String redirectUrl = "/signup-page?email=" + validToken.get().getEmail();
            return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl, "message", "Token is valid."));
        } else {
            // Token is invalid or expired
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired token."));
        }
    }

    @PostMapping("/completeSignup")
    public ResponseEntity<ResDTO<Object>> completeSignup(HttpServletRequest request , @RequestBody SignUpDTO signUpDTO) {
            return new ResponseEntity<>(signUpService.completeSignup(signUpDTO), HttpStatus.OK);
        }
}
