package com.opensource.cloudnest.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    static private final long jwtExpirationInMs = 86400000; // 1 day in milliseconds

    // Generate JWT Token
    public static String generateToken(String userName, Integer profileId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userName) // Set username as subject
                .claim("profileId", profileId) // Store profileId as a separate claim
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Validate JWT Token
    public static boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // Validate profileId in token with the one in the request
    public static Boolean validateProfileIdInAccessToken(HttpServletRequest request, Integer profileId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Integer tokenProfileId = getProfileIdFromToken(token);
            return tokenProfileId != null && tokenProfileId.equals(profileId);
        }
        return false;
    }

    // Get username from JWT token
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Get profileId from JWT token
    public static Integer getProfileIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("profileId", Integer.class); // Retrieve profileId from claim
    }

    // Check if the token has expired
    public static boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Get expiration date from token
    public static Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}
