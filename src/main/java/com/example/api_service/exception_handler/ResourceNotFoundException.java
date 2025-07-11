package com.example.api_service.exception_handler;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue), 
              HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
} 