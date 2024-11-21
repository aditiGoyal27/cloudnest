package com.opensource.cloudnest;

import com.opensource.cloudnest.controller.EmailVerificationController;
import com.opensource.cloudnest.entity.EmailVerification;
import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.repository.EmailVerificationRepository;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.EmailVerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmailVerificationControllerTest {

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private EmailVerificationController emailVerificationController;

    private static final String EMAIL = "test@example.com";
    private static final String OTP = "123456";
    private static final LocalDateTime OTP_CREATED_AT = LocalDateTime.now().minusMinutes(2); // within 5 minutes

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyOtp_Success() {
        // Arrange
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(EMAIL);
        emailVerification.setOtp(OTP);
        emailVerification.setCreatedAt(OTP_CREATED_AT);

        Profile profile = new Profile();
        profile.setEmail(EMAIL);
        profile.setEnabled(false);

        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(emailVerification));
        when(profileRepository.findByEmail(EMAIL)).thenReturn(Optional.of(profile));

        // Act
        String response = emailVerificationController.verifyOtp(EMAIL, OTP);

        // Assert
        assertEquals("OTP verified and profile enabled successfully", response);
        assertTrue(profile.isEnabled()); // Profile should be enabled after successful OTP verification
        verify(profileRepository).save(profile); // Verify the profile is saved
    }

    @Test
    void testVerifyOtp_Failure_InvalidOtp() {
        // Arrange
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(EMAIL);
        emailVerification.setOtp("wrongOtp");
        emailVerification.setCreatedAt(OTP_CREATED_AT);

        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(emailVerification));

        // Act
        String response = emailVerificationController.verifyOtp(EMAIL, OTP);

        // Assert
        assertEquals("Invalid or expired OTP", response);
        verify(profileRepository, never()).save(any()); // Ensure no profile is saved
    }

    @Test
    void testVerifyOtp_Failure_ExpiredOtp() {
        // Arrange
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(EMAIL);
        emailVerification.setOtp(OTP);
        emailVerification.setCreatedAt(LocalDateTime.now().minusMinutes(6)); // OTP expired

        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(emailVerification));

        // Act
        String response = emailVerificationController.verifyOtp(EMAIL, OTP);

        // Assert
        assertEquals("Invalid or expired OTP", response);
        verify(profileRepository, never()).save(any()); // Ensure no profile is saved
    }

    @Test
    void testVerifyOtp_Failure_ProfileNotFound() {
        // Arrange
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(EMAIL);
        emailVerification.setOtp(OTP);
        emailVerification.setCreatedAt(OTP_CREATED_AT);

        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(emailVerification));
        when(profileRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act
        String response = emailVerificationController.verifyOtp(EMAIL, OTP);

        // Assert
        assertEquals("Profile not found for the provided email", response);
        verify(profileRepository, never()).save(any()); // Ensure no profile is saved
    }

    @Test
    void testResendOtp_Success() {
        // Arrange
        Profile profile = new Profile();
        profile.setEmail(EMAIL);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(EMAIL);
        emailVerification.setOtp(OTP);
        emailVerification.setCreatedAt(OTP_CREATED_AT);

        when(profileRepository.findByEmail(EMAIL)).thenReturn(Optional.of(profile));
        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(emailVerification));

        // Act
        ResponseEntity<String> response = emailVerificationController.resendOtp(EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP resent successfully to the provided email", response.getBody());
        verify(emailVerificationRepository).save(any()); // Ensure the OTP is saved in the repository
        verify(emailVerificationService).sendVerificationEmail(eq(EMAIL), any()); // Ensure OTP is sent
    }

    @Test
    void testResendOtp_Failure_ProfileNotFound() {
        // Arrange
        when(profileRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = emailVerificationController.resendOtp(EMAIL);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Profile not found for the provided email", response.getBody());
        verify(emailVerificationRepository, never()).save(any()); // Ensure no OTP is saved
        verify(emailVerificationService, never()).sendVerificationEmail(any(), any()); // Ensure no OTP is sent
    }

    @Test
    void testResendOtp_Failure_EmailNotFoundInVerification() {
        // Arrange
        Profile profile = new Profile();
        profile.setEmail(EMAIL);

        when(profileRepository.findByEmail(EMAIL)).thenReturn(Optional.of(profile));
        when(emailVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = emailVerificationController.resendOtp(EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email Id not found", response.getBody());
        verify(emailVerificationRepository, never()).save(any()); // Ensure no OTP is saved
        verify(emailVerificationService, never()).sendVerificationEmail(any(), any()); // Ensure no OTP is sent
    }
}
