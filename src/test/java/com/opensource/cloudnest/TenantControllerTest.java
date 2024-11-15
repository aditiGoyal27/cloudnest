//package com.opensource.cloudnest;
//
//import com.opensource.cloudnest.configuration.JwtTokenProvider;
//import com.opensource.cloudnest.dto.ResDTO;
//import com.opensource.cloudnest.dto.TenantDTO;
//import com.opensource.cloudnest.dto.response.ResDTOMessage;
//import com.opensource.cloudnest.service.TenantService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//class TenantControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TenantService tenantService;
//
//    @MockBean
//    private JwtTokenProvider jwtTokenProvider;
//
//    private String jwtToken;
//    private TenantDTO tenantDTO;
//
//    @BeforeEach
//    void setUp() {
//        jwtToken = JwtTokenProvider.generateToken("palakabc" , 1); // Simulate a valid JWT token
//
//        // Initialize a sample TenantDTO object
//        tenantDTO = new TenantDTO();
//        tenantDTO.setOrgName("ABCZ");
//        tenantDTO.setOrgEmail("abcz9@gmail.com");
//        tenantDTO.setOrgContactNumber("2344556565");
//        tenantDTO.setOrgUnitName("jodjpur");
//        tenantDTO.setOrgLocation("Usa");
//        tenantDTO.setStatus("Active");
//        // Add more fields as per TenantDTO definition
//    }
//
//    @Test
//    @WithMockUser(roles = "SUPER_ADMIN")
//    void testCreateTenant_Success() throws Exception {
//        try {
//            when(jwtTokenProvider.validateProfileIdInAccessToken(any(HttpServletRequest.class), anyInt()))
//                    .thenReturn(true);
//
//            // Mock the tenantService response
//            when(tenantService.createTenant(anyInt(), any(TenantDTO.class)))
//                    .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, tenantDTO));
//
//            // Perform the POST request and validate the response
//            mockMvc.perform(post("/tenant/create/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("{ \"name\": \"Sample Tenant\", \"address\": \"Sample Address\" }")
//                            .header("Authorization", "Bearer valid-token"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value(ResDTOMessage.SUCCESS))
//                    .andExpect(jsonPath("$.data.name").value("Sample Tenant"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @WithMockUser(roles = "SUPER_ADMIN")
//    void testCreateTenant_InvalidToken() throws Exception {
//        // Mock the JWT token validation to return false
//        when(jwtTokenProvider.validateProfileIdInAccessToken(any(HttpServletRequest.class), anyInt())).thenReturn(false);
//
//        // Perform the POST request and expect failure due to invalid token
//        mockMvc.perform(post("/tenant/create/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"name\": \"Sample Tenant\", \"address\": \"Sample Address\" }")
//                        .header("Authorization", "Bearer invalid-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(false)))
//                .andExpect(jsonPath("$.message", is(ResDTOMessage.FAILURE)))
//                .andExpect(jsonPath("$.data", is("Invalid Data")));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testAssignTenantAdmin_Success() throws Exception {
//        // Mock the JWT token validation to return true
//        when(jwtTokenProvider.validateProfileIdInAccessToken(any(HttpServletRequest.class), anyInt())).thenReturn(true);
//
//        // Mock the tenantService response
//        when(tenantService.assignTenantAdmin(any(Long.class), anyInt()))
//                .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "Admin assigned successfully"));
//
//        // Perform the POST request and validate the response
//        mockMvc.perform(post("/tenant/assignAdmin")
//                        .param("tenantId", "1")
//                        .param("adminId", "2")
//                        .header("Authorization", "Bearer valid-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is(ResDTOMessage.SUCCESS)))
//                .andExpect(jsonPath("$.data", is("Admin assigned successfully")));
//    }
//
//    @Test
//    @WithMockUser(roles = "SUPER_ADMIN")
//    void testDeleteTenant_Success() throws Exception {
//        // Mock the JWT token validation to return true
//        when(jwtTokenProvider.validateProfileIdInAccessToken(any(HttpServletRequest.class), anyInt())).thenReturn(true);
//
//        // Mock the tenantService response
//        when(tenantService.deleteTenant(any(Long.class)))
//                .thenReturn(new ResDTO<>(true, ResDTOMessage.SUCCESS, "Tenant deleted successfully"));
//
//        // Perform the DELETE request and validate the response
//        mockMvc.perform(delete("/tenant/delete/1")
//                        .param("tenantId", "1")
//                        .header("Authorization", "Bearer valid-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is(ResDTOMessage.SUCCESS)))
//                .andExpect(jsonPath("$.data", is("Tenant deleted successfully")));
//    }
//}
