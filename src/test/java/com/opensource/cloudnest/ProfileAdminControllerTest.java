package com.opensource.cloudnest;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.controller.ProfileAdminController;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.ProfileAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        jwtToken = JwtTokenProvider.generateToken("testUser" , 1); // Simulate a valid JWT token

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