package com.opensource.cloudnest;
import com.opensource.cloudnest.controller.ProfileAdminController;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.service.ProfileAdminService;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.configuration.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.SignatureException;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProfileAdminControllerTest {

    @InjectMocks
    private ProfileAdminController profileAdminController;

    @Mock
    private ProfileAdminService profileAdminService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HttpServletRequest request;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String jwtToken;
    private final Integer superAdminId = 1;

    private SignUpDTO signUpDTO;

    @BeforeEach
    void setUp() {
        jwtToken = JwtTokenProvider.generateToken("aditiabc" , 1); // Simulate a valid JWT token

        signUpDTO = new SignUpDTO();
        signUpDTO.setUserName("testAdmin");
        signUpDTO.setEmail("test@admin.com");
        signUpDTO.setPassword("password");
    }
    @Test
    void testCreateAdmin_Success() {
        // Arrange: Mock the service behavior
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,"Admin created Successfully");

        when(profileAdminService.signUpAdmin(eq(signUpDTO), eq(superAdminId))).thenReturn(mockResponse);

        // Act: Call the controller method
        ResDTO<Object> result = profileAdminController.createAdmin(signUpDTO, superAdminId , request);

        // Assert: Verify the result
        assertNotNull(result);
        assertEquals("true", result.isSuccess());

        // Verify interactions
        verify(profileAdminService).signUpAdmin(eq(signUpDTO), eq(superAdminId));
    }

}