package com.opensource.cloudnest;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.controller.ViewController;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ViewControllerTest {

    @Mock
    private ViewService viewService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private ViewController viewController;

    private static final Integer ADMIN_ID = 1;
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewProfile_Success() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Profile data");

        when(viewService.viewProfile(ADMIN_ID, PAGE, SIZE)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = viewController.viewProfile(request, PAGE, SIZE, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Profile data", response.getPayload());

        // Verify service method call
        verify(viewService).viewProfile(ADMIN_ID, PAGE, SIZE);
    }

    @Test
    void testViewProfile_Failure_InvalidData() {
        // Arrange
        when(jwtTokenProvider.validateProfileIdInAccessToken(request, ADMIN_ID)).thenReturn(false);

        // Act
        ResDTO<Object> response = viewController.viewProfile(request, PAGE, SIZE, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResDTOMessage.FAILURE, response.getMessage());
        assertEquals("Invalid Data", response.getPayload());

        // Verify service method was not called
        verify(viewService, never()).viewProfile(ADMIN_ID, PAGE, SIZE);
    }

    @Test
    void testViewProfile_WithoutPageAndSize() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Profile data");

        when(viewService.viewProfile(ADMIN_ID, 0, 10)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = viewController.viewProfile(request, 0, 10, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Profile data", response.getPayload());

        // Verify service method call with default parameters
        verify(viewService).viewProfile(ADMIN_ID, 0, 10);
    }

    @Test
    void testViewProfile_PageWithCustomSize() {
        // Arrange
        ResDTO<Object> mockResponse = new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Profile data");

        when(viewService.viewProfile(ADMIN_ID, 2, 5)).thenReturn(mockResponse);

        // Act
        ResDTO<Object> response = viewController.viewProfile(request, 2, 5, ADMIN_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ResDTOMessage.SUCCESS, response.getMessage());
        assertEquals("Profile data", response.getPayload());

        // Verify service method call with custom page and size
        verify(viewService).viewProfile(ADMIN_ID, 2, 5);
    }
}
