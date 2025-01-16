package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.TenantDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.DashBoardResponse;
import com.opensource.cloudnest.dto.response.ProfileInfoResponse;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.dto.response.TenantResponse;
import com.opensource.cloudnest.entity.*;
import com.opensource.cloudnest.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownServiceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;
    @Transactional
    public ResDTO<Object> createTenant(Integer adminId , TenantDTO tenantDTO) {
        String tenantName = tenantDTO.getTenantName();
        String orgName = tenantDTO.getOrgName();
        String orgAdminName = tenantDTO.getOrgAdminName();
        String orgAdminEmail = tenantDTO.getOrgAdminEmail();
        Profile profile = new Profile();
        profile.setName(orgAdminName);
        profile.setEmail(orgAdminEmail);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
        role.ifPresent(profile::setRole);
        Optional<Profile> optionalProfileSuperAdmin = profileRepository.findById(adminId);
        optionalProfileSuperAdmin.ifPresent(profile::setSuperAdmin);
        profileRepository.save(profile);
        Tenant tenant = new Tenant();
        tenant.setOrgName(orgName);
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setTenantName(tenantName);
        Optional<Profile> optionalProfile = profileRepository.findByEmail(orgAdminEmail);
        optionalProfile.ifPresent(tenant::setTenantAdmin);
        tenant.setStatus(Tenant.Status.ACTIVE);
        tenantRepository.save(tenant);
        if(optionalProfile.isPresent()){
            Profile profile1 = optionalProfile.get();
            Optional<Tenant> optionalTenant = tenantRepository.findByTenantName(tenantName);
            optionalTenant.ifPresent(profile1::setTenant);
            profileRepository.save(profile1);
        }
        String token = tokenService.createToken(orgAdminEmail);
        emailService.sendSignUpLink(orgAdminEmail, token);
        // Save token to database
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SIGN_UP_SUCCESS, "Tenant created successfully");
    }

    @Transactional
    public ResDTO<Object> updateTenant(Integer superAdminId ,Long tenantId,TenantDTO tenantDTO) {
        String tenantName = tenantDTO.getTenantName();
        String orgName = tenantDTO.getOrgName();
        String orgAdminName = tenantDTO.getOrgAdminName();
        String orgAdminEmail = tenantDTO.getOrgAdminEmail();
        Optional<Profile>   optionalProfile = profileRepository.findByEmail(orgAdminEmail);
        if(optionalProfile.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Profile not found");
        }
        Profile profile = optionalProfile.get();
        profile.setName(orgAdminName);
        profile.setEmail(orgAdminEmail);
        Optional<Role> role = roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
        role.ifPresent(profile::setRole);
        profileRepository.save(profile);
        Optional<Tenant> optionalTenant = tenantRepository.findById(tenantId);
        if(optionalTenant.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant not found");
        }

        Tenant tenant = optionalTenant.get();
        tenant.setOrgName(orgName);
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setTenantName(tenantName);
        optionalProfile.ifPresent(tenant::setTenantAdmin);
        tenant.setUpdatedAt(LocalDateTime.now());
       // String token = tokenService.createToken(orgAdminEmail);
       // emailService.sendSignUpLink(orgAdminEmail, token);
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Tenant updated successfully");
    }

    @Transactional
    public ResDTO<Object> softDeleteTenant(Long organizationId) {
        // Check if the tenant exists
        Optional<Tenant> optionalTenant = tenantRepository.findById(organizationId);
        if (optionalTenant.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant not found");
        }

        Tenant tenant = optionalTenant.get();
        List<Profile> users = tenant.getUsers();
        for (Profile user : users) {
            user.setEnabled(false);
        }
        tenant.setStatus(Tenant.Status.INACTIVE);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.DELETED_SUCCESSFULLY, "Tenant deleted successfully");
    }

    @Transactional
    public ResDTO<Object> suspendTenant(Long tenantId) {
        Optional<Tenant> optionalOrganizationUnit = tenantRepository.findById(tenantId);
        if(optionalOrganizationUnit.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Record Does Not exists");
        }
        Tenant tenant = optionalOrganizationUnit.get();
        tenant.setStatus(Tenant.Status.SUSPENDED);
        tenant.setUpdatedAt(LocalDateTime.now());
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
        tenant.setStatus(Tenant.Status.ACTIVE);
        tenant.setUpdatedAt(LocalDateTime.now());
        tenantRepository.save(tenant);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.UPDATED_SUCCESSFULLY, "Tenant reactivated successfully");
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
            Optional<Tenant> tenant = tenantRepository.findByTenantAdmin(optionalProfile.get());
            if(tenant.get().getStatus().equals(Tenant.Status.INACTIVE)) {
                return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant Data cannot be viewed as it is deleted");
            }
            DashBoardResponse dashBoardResponse = createDashBoardResponse(tenant.get());
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
                        .email(profile.getEmail())
                        .role(profile.getRole().getName().toString())
                        .status(profile.getStatus())
                        .enabled(profile.isEnabled())
                        .build();
                profileInfoResponseList.add(profileInfoResponse);
            }
             dashBoardResponse = new DashBoardResponse(
                    tenant.getId(),
                    tenant.getTenantName(),
                    tenant.getOrgName(),
                    tenant.getTenantAdmin().getName(),
                    tenant.getTenantAdmin().getEmail(),
                    profileInfoResponseList,
                    tenant.getBillingDetails()!=null ? tenant.getBillingDetails().getAmount() : 0);

        // Assuming this method should return a single DashBoardResponse or modify as needed
        return dashBoardResponse;
    }

    private List<DashBoardResponse> createDashBoardResponseForSuperAdmins(List<Tenant> tenantList) {
        List<DashBoardResponse> dashBoardResponseList = new ArrayList<>();
        for (Tenant tenant : tenantList) {
            List<ProfileInfoResponse> profileInfoResponseList = new ArrayList<>();
            if(tenant.getUsers()!=null && tenant.getStatus().equals(Tenant.Status.ACTIVE)) {
                for (Profile profile : tenant.getUsers()) { // Assuming `getProfiles()` returns the profiles for the tenant
                    ProfileInfoResponse profileInfoResponse = ProfileInfoResponse.builder()
                            .id(profile.getId())
                            .name(profile.getName())
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
                    .tenantName(tenant.getTenantName())
                    .orgAdminName(tenant.getTenantAdmin().getName())
                    .orgAdminEmail(tenant.getTenantAdmin().getEmail())
                    .profileInfoResponseList(profileInfoResponseList)
                    .billingAmount(tenant.getBillingDetails() != null ? tenant.getBillingDetails().getAmount() : 0) // Assuming `getBillingAmount()` exists
                    .build();

            dashBoardResponseList.add(dashBoardResponse);
        }

        // Assuming this method should return a single DashBoardResponse or modify as needed
        return  dashBoardResponseList; // Modify based on your requirement
    }
/*
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
    }*/

    /*@Transactional
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
    }*/

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

    public ResDTO<List<TenantResponse>> getAllTenants() {
      List<Tenant> tenantList = tenantRepository.findAll();
      tenantList = tenantList.stream().filter(tenant -> tenant.getStatus().equals(Tenant.Status.ACTIVE)).collect(Collectors.toList());
      List<TenantResponse> tenantResponses = createTenantResponse(tenantList);
      return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, tenantResponses);
    }

    public ResDTO<List<TenantResponse>> getFilterTenants(String orgName, String orgAdminName) {
        List<Tenant> tenantList = tenantRepository.findAll();
        // Convert inputs to lowercase for case-insensitive comparison
        String lowerOrgName = orgName != null ? orgName.toLowerCase() : null;
        String lowerOrgAdminName = orgAdminName != null ? orgAdminName.toLowerCase() : null;

        List<Tenant> filteredTenants = tenantList.stream()
                .filter(tenant -> {
                    // Filter by orgName (case-insensitive)
                    if (lowerOrgName == null || lowerOrgName.isEmpty()) {
                        return true;
                    }
                    String tenantOrgName = tenant.getOrgName();
                    return tenantOrgName != null && tenantOrgName.toLowerCase().contains(lowerOrgName);
                })
                .filter(tenant -> {
                    // Filter by orgAdminName (case-insensitive)
                    if (lowerOrgAdminName == null || lowerOrgAdminName.isEmpty()) {
                        return true;
                    }
                    if (tenant.getTenantAdmin() == null || tenant.getTenantAdmin().getName() == null) {
                        return false;
                    }
                    String tenantAdminName = tenant.getTenantAdmin().getName();
                    return tenantAdminName.toLowerCase().contains(lowerOrgAdminName);
                })
                .collect(Collectors.toList());

        // Convert filtered tenants to response DTOs
        List<TenantResponse> tenantResponses = createTenantResponse(filteredTenants);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, tenantResponses);
    }


    private List<TenantResponse> createTenantResponse(List<Tenant> tenantList) {
        List<TenantResponse> tenantResponses = new ArrayList<>();
        for (Tenant tenant : tenantList) {
            TenantResponse tenantResponse = TenantResponse.builder()
                    .id(tenant.getId())
                    .tenantName(tenant.getTenantName())
                    .orgAdminName(tenant.getTenantAdmin().getName())
                    .orgAdminEmail(tenant.getTenantAdmin().getEmail())
                    .orgName(tenant.getOrgName())
                    .createdAt(tenant.getCreatedAt())
                    .updatedAt(tenant.getUpdatedAt())
                    .build();
            tenantResponses.add(tenantResponse);
        }
        return tenantResponses;
    }

    @Transactional
    public ResDTO<Object> deleteTenant(Long organizationId) {
        // Check if the tenant exists
        Optional<Tenant> optionalTenant = tenantRepository.findById(organizationId);
        if (optionalTenant.isEmpty()) {
            return new ResDTO<>(Boolean.FALSE, ResDTOMessage.RECORD_NOT_FOUND, "Tenant not found");
        }

        Tenant tenant = optionalTenant.get();
        List<Profile> users = tenant.getUsers();
        for (Profile user : users) {

            user.setEnabled(false);
        }
        tenantRepository.delete(tenant);
        //tenant.setStatus(Tenant.Status.INACTIVE);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.DELETED_SUCCESSFULLY, "Tenant deleted successfully");
    }
}
