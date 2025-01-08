package com.opensource.cloudnest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.controller.TenantController;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.TenantService;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.TenantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

public class TenantControllerTest {

    @InjectMocks
    private TenantController tenantController;

    @Mock
    private TenantService tenantService;

    @Mock
    private HttpServletRequest request;

    private String jwtToken;
    private TenantDTO tenantDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToken = JwtTokenProvider.generateToken("palakabc" , 1); // Simulate a valid JWT token
        tenantDTO = new TenantDTO();
        tenantDTO.setStatus("active");
        tenantDTO.setOrgName("abcTest");
        tenantDTO.setOrgLocation("usa");
        tenantDTO.setOrgEmail("abc@sdt.com");
        tenantDTO.setOrgUnitName("kll");
    }

    // Utility Constants
    private final Integer SUPER_ADMIN_ID = 1;
    private final Long TENANT_ID = 123L;
    private final Integer ADMIN_ID = 2;

    @Test
    void testCreateTenant_Success() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,"Tenant created Successfully");

        //when(JwtTokenProvider.validateProfileIdInAccessToken(request, SUPER_ADMIN_ID)).thenReturn(true);
        when(tenantService.createTenant(SUPER_ADMIN_ID, tenantDTO)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = tenantController.createTenant(request, SUPER_ADMIN_ID, tenantDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Tenant Created", response.getPayload());

        verify(tenantService).createTenant(SUPER_ADMIN_ID, tenantDTO);
    }

    @Test
    void testCreateTenant_Failure() {
        // Arrange
        TenantDTO tenantDTO = new TenantDTO();
      //  when(JwtTokenProvider.validateProfileIdInAccessToken(request, SUPER_ADMIN_ID)).thenReturn(false);

        // Act
        ResDTO<Object> response = tenantController.createTenant(request, SUPER_ADMIN_ID, tenantDTO);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());

        verifyNoInteractions(tenantService);
    }

    @Test
    void testAssignTenantAdmin_Success() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,"Admin assigned Successfully");

       // when(JwtTokenProvider.validateProfileIdInAccessToken(request, ADMIN_ID)).thenReturn(true);
        when(tenantService.assignTenantAdmin(TENANT_ID, ADMIN_ID)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = tenantController.assignTenantAdmin(request, TENANT_ID, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Admin Assigned", response.getPayload());

        verify(tenantService).assignTenantAdmin(TENANT_ID, ADMIN_ID);
    }

    @Test
    void testAssignTenantAdmin_Failure() {
        // Arrange
      //  when(JwtTokenProvider.validateProfileIdInAccessToken(request, ADMIN_ID)).thenReturn(false);

        // Act
        ResDTO<Object> response = tenantController.assignTenantAdmin(request, TENANT_ID, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());

        verifyNoInteractions(tenantService);
    }

    @Test
    void testDeleteTenant_Success() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,"Tenant deleted Successfully");

      //  when(JwtTokenProvider.validateProfileIdInAccessToken(request, SUPER_ADMIN_ID)).thenReturn(true);
        when(tenantService.deleteTenant(TENANT_ID)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = tenantController.deleteTenant(TENANT_ID, request, SUPER_ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Tenant Deleted", response.getPayload());

        verify(tenantService).deleteTenant(TENANT_ID);
    }

    @Test
    void testDeleteTenant_Failure() {
        // Arrange
       // when(JwtTokenProvider.validateProfileIdInAccessToken(request, SUPER_ADMIN_ID)).thenReturn(false);

        // Act
        ResDTO<Object> response = tenantController.deleteTenant(TENANT_ID, request, SUPER_ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());

        verifyNoInteractions(tenantService);
    }
}
