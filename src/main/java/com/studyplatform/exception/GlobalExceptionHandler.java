package com.studyplatform.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        
        // Get the first validation error message
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        response.put("message", errorMessage);
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        // Log the exception
        System.err.println("Unhandled exception: " + ex.getMessage());
        ex.printStackTrace();
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", "An unexpected error occurred. Please try again.");
        
        return ResponseEntity.badRequest().body(response);
    }
}