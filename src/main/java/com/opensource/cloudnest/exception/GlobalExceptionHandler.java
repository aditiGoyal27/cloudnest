//package com.opensource.cloudnest.exception;
//
//import com.opensource.cloudnest.dto.ResDTO;
//import com.opensource.cloudnest.dto.response.ResDTOMessage;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle specific exception (e.g., custom exception)
//    @ExceptionHandler(GlobalCustomException.class)
//    public ResponseEntity<ResDTO<String>> handleCustomException(GlobalCustomException ex) {
//        ResDTO<String> response = new ResDTO<>(false, ResDTOMessage.FAILURE, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle general exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResDTO<String>> handleGeneralException(Exception ex) {
//        ResDTO<String> response = new ResDTO<>(false, ResDTOMessage.FAILURE, "An unexpected error occurred");
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // Handle specific Spring exceptions (e.g., validation errors, data not found)
//    @ExceptionHandler(org.springframework.validation.BindException.class)
//    public ResponseEntity<ResDTO<String>> handleValidationException(org.springframework.validation.BindException ex) {
//        ResDTO<String> response = new ResDTO<>(false, ResDTOMessage.FAILURE, "Validation failed: " + ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle 404 errors (resource not found)
//    @ExceptionHandler(GlobalCustomException.class)
//    public ResponseEntity<ResDTO<String>> handleResourceNotFoundException(GlobalCustomException ex) {
//        ResDTO<String> response = new ResDTO<>(false, ResDTOMessage.FAILURE, "Resource not found: " + ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    // Handle any other unchecked exceptions
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ResDTO<String>> handleRuntimeException(RuntimeException ex) {
//        ResDTO<String> response = new ResDTO<>(false, ResDTOMessage.FAILURE, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//}
