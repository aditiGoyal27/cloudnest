package com.opensource.cloudnest.configuration;

import com.opensource.cloudnest.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Extract Authorization header
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Integer profileId = null;

        // Validate Authorization header
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                // Extract username and profileId
                username = jwtTokenProvider.getUsernameFromToken(token);
                profileId = jwtTokenProvider.getProfileIdFromToken(token);
            } catch (Exception ex) {
                // Handle invalid tokens gracefully
                logger.error("Failed to parse token: " + ex.getMessage());
            }
        }

        // Validate user and token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token
            if (jwtTokenProvider.validateToken(token)) {
                // Create authentication object and set in SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Set profileId attribute in request (optional, for downstream use)
                request.setAttribute("profileId", profileId);
            } else {
                logger.warn("JWT token validation failed");
            }
        }

        // Proceed with the filter chain
        chain.doFilter(request, response);
    }
}
