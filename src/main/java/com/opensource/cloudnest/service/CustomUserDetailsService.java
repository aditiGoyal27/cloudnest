package com.opensource.cloudnest.service;


import com.opensource.cloudnest.configuration.CustomUserDetails;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    public CustomUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Assign the single role
        String role = profile.getRole().getName(); // Assuming `user.getRole()` fetches the single role entity
        return new CustomUserDetails(profile.getId(), profile.getEmail(), profile.getPassword(), role);
    }
}
