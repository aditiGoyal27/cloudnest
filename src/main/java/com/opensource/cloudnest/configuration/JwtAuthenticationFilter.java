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

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Integer profileId = null;

        try {
            // Validate Authorization header
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);

                // Extract username and profileId from token
                username = jwtTokenProvider.getUsernameFromToken(token);
                profileId = jwtTokenProvider.getProfileIdFromToken(token);

                // Validate token
                if (!jwtTokenProvider.validateToken(token)) {
                    throw new InvalidTokenException("JWT token validation failed");
                }
            }

            // Authenticate user if token and username are valid
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create authentication object and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Set profileId in the request for downstream use
                request.setAttribute("profileId", profileId);
            }

            // Proceed with the filter chain
            chain.doFilter(request, response);

        } catch (Exception ex) {
            logger.error("Error during JWT authentication: {} " + ex.getMessage());
            // Send error response
            handleException(response, ex);
        }
    }


    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // Ensure proper encoding for special characters
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Default to 401 Unauthorized

        // Determine status code dynamically based on exception type
        if (ex instanceof InvalidTokenException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (ex instanceof ForbiddenException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        }

        // Construct a structured error response
        String jsonError = String.format(
                ex.getMessage()
        );

        // Write JSON response
        response.getWriter().write(jsonError);
        response.getWriter().flush();
    }

}
