package com.opensource.cloudnest;


import com.opensource.cloudnest.controller.InviteUserController;
import com.opensource.cloudnest.service.InvitationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InviteUserControllerTest {

    @Mock
    private InvitationService invitationService;

    @InjectMocks
    private InviteUserController inviteUserController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inviteUserController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simulating a user with ADMIN role
    void testInviteUser_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        String name = "Test User";
        String expectedResponse = "Invitation sent to test@example.com";

        // Mock the service method
        when(invitationService.inviteUser(email, name)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/app/invite/user")
                        .param("email", email)
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(invitationService).inviteUser(email, name); // Verify that the service method was called
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testInviteUser_Failure_NotAdmin() throws Exception {
        // Simulate a user without 'ADMIN' role
        mockMvc.perform(post("/app/invite/user")
                        .param("email", "test@example.com")
                        .param("name", "Test User"))
                .andExpect(status().isForbidden()); // Expected to return 403 Forbidden
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterUser_Success() throws Exception {
        // Arrange
        String token = "someToken";
        String password = "securePassword";
        String expectedResponse = "User registered successfully";

        // Mock the service method
        when(invitationService.completeRegistration(token, password)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/app/register/user")
                        .param("token", token)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(invitationService).completeRegistration(token, password); // Verify that the service method was called
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterUser_Failure_NotAdmin() throws Exception {
        // Simulate a user without 'ADMIN' role
        mockMvc.perform(post("/app/register/user")
                        .param("token", "someToken")
                        .param("password", "securePassword"))
                .andExpect(status().isForbidden()); // Expected to return 403 Forbidden
    }

    @Test
    void testInviteUser_NoAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/app/invite/user")
                        .param("email", "test@example.com")
                        .param("name", "Test User"))
                .andExpect(status().isUnauthorized()); // Expected to return 401 Unauthorized when no user is authenticated
    }

    @Test
    void testRegisterUser_NoAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/app/register/user")
                        .param("token", "someToken")
                        .param("password", "securePassword"))
                .andExpect(status().isUnauthorized()); // Expected to return 401 Unauthorized when no user is authenticated
    }
}
