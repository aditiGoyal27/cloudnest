
package com.opensource.cloudnest;

import com.opensource.cloudnest.configuration.ForbiddenException;
import com.opensource.cloudnest.configuration.InvalidTokenException;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Invalid Token Exception (401 Unauthorized)
    @ExceptionHandler(SignatureException.class)
    public ResDTO<Object> handleSignatureInvalidException(InvalidTokenException ex) {
        ErrorResponse errorResponse = new ErrorResponse("FAILURE", ex.getMessage());
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, errorResponse);  // 401 Unauthorized
    }

    // Handle Forbidden Exception (403 Forbidden)
    @ExceptionHandler(ForbiddenException.class)
    public ResDTO<Object> handleForbiddenException(ForbiddenException ex) {
        ErrorResponse errorResponse = new ErrorResponse("FAILURE", ex.getMessage());
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, errorResponse);  // 403 Forbidden
    }

    // Handle any other unexpected exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResDTO<Object> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("FAILURE", ex.getMessage());
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, errorResponse);  // 500 Internal Server Error
    }

    // Custom Error Response
    public static class ErrorResponse {
        private String status;
        private String message;

        public ErrorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}


