/*
package com.opensource.cloudnest.service;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String authenticateAndGenerateToken(String username, String password) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        // Load user details (optional step to fetch additional information)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Generate and return the JWT token
        return jwtTokenProvider.generateToken(authentication);
    }
}
*/
