package com.example.api_service.exception_handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    
    private final HttpStatus status;
    private final String errorCode;
    
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = status.getReasonPhrase();
    }
    
    public ApiException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    
    public ApiException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = status.getReasonPhrase();
    }
} 