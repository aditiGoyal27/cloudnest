package com.opensource.cloudnest;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.controller.UserAccountController;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserAccountControllerTest {

    @InjectMocks
    private UserAccountController userAccountController;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;

    private String jwtToken;
    private SignUpDTO signUpDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToken = JwtTokenProvider.generateToken("aditiabc" , 1); // Simulate a valid JWT token

        signUpDTO = new SignUpDTO();
        signUpDTO.setUserName("testUser");
        signUpDTO.setEmail("test@user.com");
        signUpDTO.setPassword("password");
    }

    @Test
    void testCreateUser_Success() {
        // Mocking the request
        SignUpDTO signUpDTO = new SignUpDTO();
        int adminId = 1;

        when(userAccountService.createUser(signUpDTO, adminId)).thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "user created"));

        ResDTO<Object> response = userAccountController.createUser(httpServletRequest, signUpDTO, adminId);

        assertTrue(response.getSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("User Created", response.getPayload());
    }

    @Test
    void testCreateUser_InvalidToken() {
        // Mocking the request
        SignUpDTO signUpDTO = new SignUpDTO();
        int adminId = 1;

        ResDTO<Object> response = userAccountController.createUser(httpServletRequest, signUpDTO, adminId);

        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());
    }

    @Test
    void testUpdateUser_Success() {
        // Mocking the request
        SignUpDTO signUpDTO = new SignUpDTO();
        int adminId = 1;

        when(userAccountService.updateUser(signUpDTO, adminId)).thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "user_updated"));

        ResDTO<Object> response = userAccountController.updateUser(httpServletRequest, signUpDTO, adminId);

        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("User Updated", response.getPayload());
    }

    @Test
    void testUpdateUser_InvalidToken() {
        // Mocking the request
        SignUpDTO signUpDTO = new SignUpDTO();
        int adminId = 1;

        ResDTO<Object> response = userAccountController.updateUser(httpServletRequest, signUpDTO, adminId);

        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid data", response.getPayload());
    }

    @Test
    void testDeleteUser_Success() {
        int adminId = 1;
        int userId = 2;

        when(userAccountService.deleteUser(adminId, userId)).thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "delete user"));


        ResDTO<Object> response = userAccountController.deleteUser(httpServletRequest, adminId, userId);

        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("User Deleted", response.getPayload());
    }

    @Test
    void testDeleteUser_InvalidToken() {
        int adminId = 1;
        int userId = 2;


        ResDTO<Object> response = userAccountController.deleteUser(httpServletRequest, adminId, userId);

        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());
    }

    @Test
    void testSuspendUser_Success() {
        int adminId = 1;
        int userId = 2;

        when(userAccountService.suspendUser(adminId, userId)).thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "user_suspended"));

        ResDTO<Object> response = userAccountController.suspendUser(httpServletRequest, adminId, userId);

        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("User Suspended", response.getPayload());
    }

    @Test
    void testSuspendUser_InvalidToken() {
        int adminId = 1;
        int userId = 2;

        ResDTO<Object> response = userAccountController.suspendUser(httpServletRequest, adminId, userId);

        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());
    }
}
