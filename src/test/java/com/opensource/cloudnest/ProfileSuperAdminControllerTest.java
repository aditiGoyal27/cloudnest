package com.opensource.cloudnest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.controller.ProfileSuperAdminController;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.ProfileSuperAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileSuperAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProfileSuperAdminService signUpAdminService;

    @InjectMocks
    private ProfileSuperAdminController profileSuperAdminController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileSuperAdminController).build();
    }

    @Test
    void testCreateSuperAdmin() throws Exception {
        // Prepare sample data for the test
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setName("testSuperAdmin");
        signUpDTO.setEmail("testAdmin4@gmail.com");
        signUpDTO.setPassword("12345678");
        signUpDTO.setUserName("superAdmin4");
        signUpDTO.setContactNumber("123444566");

        // Mock the service call response
        Mockito.when(signUpAdminService.signUpSuperAdmin(Mockito.any(SignUpDTO.class)))
                .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, null));

        // Perform the POST request and verify the response
        mockMvc.perform(post("/profile/superAdmin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("SUCCESS"));
    }    // Test for the "getProfileDetails" endpoint with valid token
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void testGetProfileDetails_ValidToken() throws Exception {
        // Mock the validateProfileIdInAccessToken method to return true
        try (MockedStatic<JwtTokenProvider> jwtTokenProviderMock = mockStatic(JwtTokenProvider.class)) {
            jwtTokenProviderMock.when(() -> JwtTokenProvider.validateProfileIdInAccessToken(Mockito.any(HttpServletRequest.class), Mockito.anyInt()))
                    .thenReturn(true);

            // Mock the service call to getProfileDetails
            Mockito.when(signUpAdminService.getProfileDetails())
                    .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, null));

            // Perform the GET request and verify the response
            mockMvc.perform(get("/profile/superAdmin/getProfileDetails/1")
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("SUCCESS"));
        }
    }

    // Test for the "getProfileDetails" endpoint with invalid token
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void testGetProfileDetails_InvalidToken() throws Exception {
        // Mock the validateProfileIdInAccessToken method to return false
        try (MockedStatic<JwtTokenProvider> jwtTokenProviderMock = mockStatic(JwtTokenProvider.class)) {
            jwtTokenProviderMock.when(() -> JwtTokenProvider.validateProfileIdInAccessToken(Mockito.any(HttpServletRequest.class), Mockito.anyInt()))
                    .thenReturn(false);

            // Perform the GET request and verify the response
            mockMvc.perform(get("/profile/superAdmin/getProfileDetails/1")
                            .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("RECORD_NOT_FOUND"));
                    // Check if "data" exists only if it should be present; otherwise, assert its absence.
                    //.andExpect(jsonPath("$.data").value("user data not found"));
        }
    }

    // Test for the "updateProfileDetails" endpoint
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void testUpdateProfileDetails() throws Exception {
        // Sample SignUpDTO to pass in the request
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setOrganizationName("äbc");
        signUpDTO.setName("testAVCD");
        // Mock the service call to updateProfileDetails
        Mockito.when(signUpAdminService.updateProfileDetails(Mockito.anyInt(), Mockito.any(SignUpDTO.class)))
                .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, null));

        // Perform the POST request and verify the response
        mockMvc.perform(post("/profile/superAdmin/updateProfileDetails/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("SUCCESS"));
    }
}
