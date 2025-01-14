package com.opensource.cloudnest.configuration;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.ProfileRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;


@Component
@Service
public class JwtTokenProvider {


    private static final String BASE64_SECRET_KEY = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(BASE64_SECRET_KEY));
    static private final long jwtExpirationInMs = 86400000; // 1 day in milliseconds
    private org.slf4j.Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    // Generate JWT Token
    public static String generateToken(String userName, Integer profileId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userName) // Set username as subject
                .claim("profileId", profileId) // Store profileId as a separate claim
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY) // Verify the key
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("Token expired: " + ex.getMessage());
        } catch (SignatureException | MalformedJwtException ex) {
            logger.error("Invalid token: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Token validation failed: " + ex.getMessage());
        }
        return false; // Invalid token
    }

    // Validate profileId in token with the one in the request
    public static Boolean validateProfileIdInAccessToken(HttpServletRequest request , @Autowired ProfileRepository profileRepository) {
        // Retrieve Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Log or throw an error for missing or malformed Authorization header
            return false;
        }

        try {
            // Extract the token
            String token = authHeader.substring(7);

            // Get profileId from the token
            Integer tokenProfileId = getProfileIdFromToken(token);
            if (tokenProfileId == null) {
                return false; // Token does not contain a valid profileId
            }

            // Validate profileId against the one from the request
            Optional<Profile> optionalProfile = profileRepository.findById(tokenProfileId);
            return optionalProfile
                    .map(profile -> profile.getId() != null && profile.getId().equals(tokenProfileId))
                    .orElse(false);
        } catch (Exception e) {
            // Log the exception and return false
            e.printStackTrace();
            return false;
        }
    }

    public  Integer getProfileFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7);
            return getProfileIdFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // Get username from JWT token
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Get profileId from JWT token
    public static Integer getProfileIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
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
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}
