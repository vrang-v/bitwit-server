package com.server.bitwit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ErrorCode
{
    DEFAULT("C1000", "Default Bitwit Error", BAD_REQUEST),
    
    RESOURCE_NOT_FOUND("C404", "Resource Not Found", NOT_FOUND);
    
    
    private final String code;
    
    private final String baseMessage;
    
    private final HttpStatus httpStatus;
    
    ErrorCode(String code, String baseMessage, HttpStatus httpStatus)
    {
        this.code        = code;
        this.baseMessage = baseMessage;
        this.httpStatus  = httpStatus;
    }
}
