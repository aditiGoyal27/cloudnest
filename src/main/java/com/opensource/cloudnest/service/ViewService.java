package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ProfileInfoResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.dto.response.TenantResponse;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Relation;
import com.opensource.cloudnest.entity.Tenant;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.repository.RelationRepository;
import com.opensource.cloudnest.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViewService {

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public ResDTO<Object> viewProfile( Integer adminId , int page , int size) {
        Optional<Profile> optionalProfile = profileRepository.findById(adminId);
        if (optionalProfile.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "No data found");
        }
        List<Profile> userProfiles = profileRepository.findByAdmin(optionalProfile.get());
        List<Relation> relationList = relationRepository.findByAdminId(optionalProfile.get());
        List<ProfileInfoResponse> profileInfoResponses = new ArrayList<>();

        // Fetch profiles for each userId linked to the adminId
        for (Profile profile : userProfiles) {
            ProfileInfoResponse profileInfoResponse = new ProfileInfoResponse(
                    profile.getId(),
                    profile.getName(),
                    profile.getEmail(),
                    profile.getRole().getName().toString(),
                    profile.getStatus(),
                    profile.getTenant() != null ? profile.getTenant().getId() : 0,
                    profile.getCreatedOn(),
                    profile.getUpdatedOn(),
                    profile.isEnabled()
            );
            profileInfoResponses.add(profileInfoResponse);
        }


        // Create a pageable instance
        Pageable pageable = PageRequest.of(page, size);

        // Calculate start and end indices for pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), profileInfoResponses.size());

        // Create sublist for pagination
        List<ProfileInfoResponse> subProfileList = profileInfoResponses.subList(start, end);

        // Create a Page object
        Page<ProfileInfoResponse> pageResponse = new PageImpl<>(subProfileList, pageable, profileInfoResponses.size());

        // Return the response wrapped in ResDTO
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, pageResponse);
    }

    public ResDTO<Object> filterProfile(String name, String email, String phoneNumber, Integer adminId) {
        // Validate admin profile
        Optional<Profile> optionalProfile = profileRepository.findById(adminId);
        if (optionalProfile.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "No data found for the given adminId");
        }

        // Fetch all relations linked to the adminId
        List<Relation> relationList = relationRepository.findByAdminId(optionalProfile.get());
        if (relationList.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "No relations found for the given adminId");
        }

        // Fetch and filter profiles based on name, email, and phoneNumber
        List<ProfileInfoResponse> profileInfoResponses = relationList.stream()
                .map(relation -> profileRepository.findById(relation.getUserId().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(profile ->
                        (name == null || profile.getName().toLowerCase().contains(name.toLowerCase())) &&
                                (email == null || profile.getEmail().toLowerCase().contains(email.toLowerCase())) &&
                                (phoneNumber == null || profile.getContactNumber().contains(phoneNumber))
                )
                .map(profile -> new ProfileInfoResponse(
                        profile.getId(),
                        profile.getName(),
                        profile.getEmail(),
                        profile.getRole().getName(),
                        profile.getStatus(),
                        profile.getTenant() != null ? profile.getTenant().getId() : 0,
                        profile.getCreatedOn(),
                        profile.getUpdatedOn(),
                        profile.isEnabled()
                ))
                .collect(Collectors.toList());

        if (profileInfoResponses.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "No matching profiles found");
        }

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, profileInfoResponses);
    }




}
