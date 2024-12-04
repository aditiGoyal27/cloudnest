package com.opensource.cloudnest;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudnestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;
	@Test
	void contextLoads() {
	}

    @BeforeEach
    void setUp() {
        // Generate a JWT token for testing, replace with your actual token generation logic
        jwtToken = JwtTokenProvider.generateToken("aditi999" , 1);  // Replace JwtTokenUtil with your JWT utility class
    }

}
