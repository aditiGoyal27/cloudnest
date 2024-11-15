//package com.opensource.cloudnest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.opensource.cloudnest.configuration.JwtTokenProvider;
//import com.opensource.cloudnest.controller.ProfileAdminController;
//import com.opensource.cloudnest.dto.ResDTO;
//import com.opensource.cloudnest.dto.SignUpDTO;
//import com.opensource.cloudnest.dto.response.ResDTOMessage;
//import com.opensource.cloudnest.repository.ProfileRepository;
//import com.opensource.cloudnest.service.ProfileAdminService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.security.SignatureException;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ProfileAdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private ProfileAdminController profileAdminController;
//
//    @Mock
//    private ProfileAdminService profileAdminService;
//
//    @Mock
//    private ProfileRepository profileRepository;
//
//    @Mock
//    private HttpServletRequest request;
//    @MockBean
//    private JwtTokenProvider jwtTokenProvider;
//
//    private String jwtToken;
//    private final Integer superAdminId = 1;
//
//    private SignUpDTO signUpDTO;
//
//    @BeforeEach
//    void setUp() {
//        jwtToken = JwtTokenProvider.generateToken("palakabc" , 1); // Simulate a valid JWT token
//
//        signUpDTO = new SignUpDTO();
//        signUpDTO.setUserName("testAdmin");
//        signUpDTO.setEmail("test@admin.com");
//        signUpDTO.setPassword("password");
//
//        // Initialize MockMvc with the controller
//        mockMvc = MockMvcBuilders.standaloneSetup(profileAdminController).build();
//    }
//
//    @Test
//    void testCreateAdmin_Success() throws Exception {
//        // Create a mock ResDTO with the expected structure
//        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS,"Admin created Successfully");
//
//        // Mocking the service layer behavior
//        when(profileAdminService.signUpAdmin(any(SignUpDTO.class), eq(1)))
//                .thenReturn(mockResponse); // Return the correct ResDTO<Object> instead of a raw Object
//
//        // Perform the mock request with the JWT token in the header
//        mockMvc.perform(post("/profile/admin/create/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(signUpDTO)) // Serialize SignUpDTO to JSON
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)) // Include the mock JWT token in Authorization header
//                .andExpect(status().isOk()) // Expect HTTP 200 OK
//                .andExpect(jsonPath("$.success").value(true)) // Example success response check
//                .andExpect(jsonPath("$.message").value("SUCCESS")); // Example message check
//    }
//
//    @Test
//    @WithMockUser(roles = "SUPER_ADMIN")
//    void testCreateAdmin_InvalidToken() throws Exception {
//        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())) // Correct: using matcher
//                .thenThrow(new SignatureException("JWT signature does not match"));
//        String invalidJwtToken = "invalidJwtToken";  // Example invalid token
//
//        mockMvc.perform(post("/profile/admin/create/{superAdminId}", superAdminId)
//                        .header("Authorization", "Bearer " + invalidJwtToken)  // Invalid token
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))  // Assuming signUpDTO is valid
//                .andExpect(status().isUnauthorized())  // Expect 401 Unauthorized for invalid token
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("INVALID_TOKEN"))
//                .andExpect(jsonPath("$.data").value("Token is invalid or expired"));
//    }
//
//
//
//    @Test
//    void testCreateAdmin_WithoutRole() throws Exception {
//        // Test without `SUPER_ADMIN` role
//        mockMvc.perform(post("/profile/admin/create/{superAdminId}", superAdminId)
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))
//                .andExpect(status().isForbidden());
//    }
//}