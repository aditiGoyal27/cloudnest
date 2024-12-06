package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.TenantDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.DashBoardResponse;
import com.opensource.cloudnest.dto.response.ProfileInfoResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.*;
import com.opensource.cloudnest.repository.TenantRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public ResDTO<Object> createTenant(Integer adminId , TenantDTO tenantDTO) {
        String orgName = tenantDTO.getOrgName();
        String email = tenantDTO.getOrgEmail();
        String contactNumber = tenantDTO.getOrgContactNumber();
        String orgUnitName = tenantDTO.getOrgUnitName();
        String orgLocation = tenantDTO.getOrgLocation();
        String status = tenantDTO.getStatus();
        Tenant tenant = new Tenant();
        tenant.setOrgUnitName(orgUnitName);
        tenant.setOrgName(orgName);
        tenant.setOrgEmail(email);
        tenant.setOrgContactNumber(contactNumber);
        tenant.setOrgLocation(orgLocation);
        tenant.setStatus(status);
        Optional<Profile> superAdmin = profileRepository.findById(adminId);
        superAdmin.ifPresent(tenant:: setAdmin);
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Tenant created successfully");
    }

    @Transactional
    public ResDTO<Object> updateTenant(TenantDTO tenantDTO) {
        String orgName = tenantDTO.getOrgName();
        String email = tenantDTO.getOrgEmail();
        String contactNumber = tenantDTO.getOrgContactNumber();
        String orgUnitName = tenantDTO.getOrgUnitName();
        String orgLocation = tenantDTO.getOrgLocation();
        String status = tenantDTO.getStatus();
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findByOrgEmail(email);

        if(optionalOrganizationUnit.isEmpty()) {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant not found");
        }

        Tenant tenant = optionalOrganizationUnit.get();

        tenant.setOrgUnitName(orgUnitName);
        tenant.setOrgName(orgName);
        tenant.setOrgEmail(email);
        tenant.setOrgContactNumber(contactNumber);
        tenant.setOrgLocation(orgLocation);
        tenant.setStatus(status);
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Tenant updated successfully");
    }
    @Transactional
    public ResDTO<Object> deleteTenant( Long organizationId) {

        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findById(organizationId);

        if (optionalOrganizationUnit.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant not found");
        }


        tenantRepository.delete(optionalOrganizationUnit.get());
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.DELETED_SUCCESSFULLY, "Tenant deleted successfully");
    }

    @Transactional
    public ResDTO<Object> suspendTenant(Long tenantId) {
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findById(tenantId);
        if(optionalOrganizationUnit.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Tenant tenant = optionalOrganizationUnit.get();
        tenant.setStatus("INACTIVE");
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "Tenant Data suspended successfully");
    }

    @Transactional
    public ResDTO<Object> reactivateTenant(Long tenantId) {
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findById(tenantId);
        if(optionalOrganizationUnit.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Tenant tenant = optionalOrganizationUnit.get();
        tenant.setStatus("ACTIVE");
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "Tenant Data Activated successfully");
    }


    @Transactional
    public ResDTO<Object> dashboard() {
        List<Tenant> tenantList = tenantRepository.findAll();
        List<DashBoardResponse> dashBoardResponseList = createDashBoardResponseForSuperAdmins(tenantList);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,dashBoardResponseList);
    }

    @Transactional
    public ResDTO<Object> dashboardTenantAdmins(Integer tenantAdminId) {
        Optional<Profile> optionalProfile = profileRepository.findById(tenantAdminId);
        if(optionalProfile.isPresent()) {
            Tenant tenant = tenantRepository.findByAdmin(optionalProfile.get());
            DashBoardResponse dashBoardResponse = createDashBoardResponse(tenant);
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, dashBoardResponse);
        }
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.FAILURE, "Tenant Data not found");
    }

    private DashBoardResponse createDashBoardResponse(Tenant tenant) {
        DashBoardResponse dashBoardResponse ;
        List<ProfileInfoResponse> profileInfoResponseList = new ArrayList<>();
            for (Profile profile : tenant.getUsers()) { // Assuming `getProfiles()` returns the profiles for the tenant
                ProfileInfoResponse profileInfoResponse = ProfileInfoResponse.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .password(profile.getPassword()) // Ensure safe handling of passwords
                        .email(profile.getEmail())
                        .role(profile.getRole().getName().toString())
                        .status(profile.getStatus())
                        .enabled(profile.isEnabled())
                        .build();
                profileInfoResponseList.add(profileInfoResponse);
            }
             dashBoardResponse = new DashBoardResponse(
                    tenant.getId(),
                    tenant.getOrgName(),
                    tenant.getOrgUnitName(),
                    tenant.getOrgLocation(),
                    tenant.getOrgEmail(),
                    tenant.getOrgContactNumber(),
                    tenant.getStatus(),
                    tenant.getCreatedAt(),
                    tenant.getUpdatedAt(),
                    tenant.getAdmin().getName(),
                    profileInfoResponseList,
                    tenant.getBillingDetails()!=null ? tenant.getBillingDetails().getAmount() : 0);

        // Assuming this method should return a single DashBoardResponse or modify as needed
        return dashBoardResponse;
    }

    private List<DashBoardResponse> createDashBoardResponseForSuperAdmins(List<Tenant> tenantList) {
        List<DashBoardResponse> dashBoardResponseList = new ArrayList<>();
        for (Tenant tenant : tenantList) {
            List<ProfileInfoResponse> profileInfoResponseList = new ArrayList<>();
            if(tenant.getUsers()!=null) {
                for (Profile profile : tenant.getUsers()) { // Assuming `getProfiles()` returns the profiles for the tenant
                    ProfileInfoResponse profileInfoResponse = ProfileInfoResponse.builder()
                            .id(profile.getId())
                            .name(profile.getName())
                            .password(profile.getPassword()) // Ensure safe handling of passwords
                            .email(profile.getEmail())
                            .role(profile.getRole().getName().toString())
                            .status(profile.getStatus())
                            .enabled(profile.isEnabled())
                            .build();
                    profileInfoResponseList.add(profileInfoResponse);
                }
            }

            DashBoardResponse dashBoardResponse = DashBoardResponse.builder()
                    .id(tenant.getId())
                    .orgName(tenant.getOrgName())
                    .orgUnitName(tenant.getOrgUnitName())
                    .orgLocation(tenant.getOrgLocation())
                    .orgEmail(tenant.getOrgEmail())
                    .orgContactNumber(tenant.getOrgContactNumber())
                    .status(tenant.getStatus())
                    .createdAt(tenant.getCreatedAt())
                    .updatedAt(tenant.getUpdatedAt())
                    .adminName(tenant.getAdmin().getName())
                    .profileInfoResponseList(profileInfoResponseList)
                    .billingAmount(tenant.getBillingDetails() != null ? tenant.getBillingDetails().getAmount() : 0) // Assuming `getBillingAmount()` exists
                    .build();

            dashBoardResponseList.add(dashBoardResponse);
        }

        // Assuming this method should return a single DashBoardResponse or modify as needed
        return  dashBoardResponseList; // Modify based on your requirement
    }

    @Transactional
    public ResDTO<Object> editTenantSettings(Integer tenantAdminId , TenantDTO tenantDTO) {
        Optional<Profile> optionalProfile = profileRepository.findById(tenantAdminId);
        if(optionalProfile.isPresent()) {
            Tenant tenant = tenantRepository.findByAdmin(optionalProfile.get());
            tenant.setOrgUnitName(tenantDTO.getOrgUnitName());
            tenant.setOrgEmail(tenantDTO.getOrgEmail());
            tenant.setOrgLocation(tenantDTO.getOrgLocation());
            tenant.setOrgContactNumber(tenantDTO.getOrgContactNumber());
            tenant.setStatus(tenantDTO.getStatus());
            tenantRepository.save(tenant);
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Data updated successfully");
        }
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.FAILURE, "Tenant Data not found");
    }

    @Transactional
    public ResDTO<Object> assignTenantAdmin(Long tenantId , Integer adminId) {
        try {
            Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
            if (optionalTenant.isPresent()) {
                Tenant tenant = optionalTenant.get();
                Optional<Profile> optionalProfile = profileRepository.findById(adminId);
                optionalProfile.ifPresent(tenant::setAdmin);
            }
        } catch (Exception e) {
            return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE, e.getMessage());
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Admin assigned successfully");
    }

    @Transactional
    public ResDTO<Object> assignUser(Long tenantId , Integer userId) {
        try {
            Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
            if (optionalTenant.isPresent()) {
                Tenant tenant = optionalTenant.get();
                Optional<Profile> optionalProfile = profileRepository.findById(userId);
                optionalProfile.ifPresent(profile -> {
                    List<Profile> currentUsers = tenant.getUsers();
                    if (currentUsers == null) {
                        currentUsers = new ArrayList<>(); // Initialize the list if it's null
                    }
                    currentUsers.add(profile); // Add the new profile to the list
                    tenant.setUsers(currentUsers); // Set the updated list back to the tenant
                    tenantRepository.save(tenant); // Save the updated tenant
                });
            }
        } catch (Exception e) {
            return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE, e.getMessage());
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "user assigned successfully");
    }
}
