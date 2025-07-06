package com.example.api_service.exception_handler;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST");
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
} 