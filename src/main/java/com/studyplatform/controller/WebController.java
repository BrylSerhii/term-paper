package com.studyplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to handle web-related endpoints
 */
@RestController
@RequestMapping("/api")
public class WebController {

    /**
     * Health check endpoint to verify the application is running
     * @return a simple response with application status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Study Platform API is running");
        return ResponseEntity.ok(response);
    }
}
