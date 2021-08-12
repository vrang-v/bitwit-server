package com.server.bitwit.module.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    DEFAULT("C1000", "Default Bitwit Error", INTERNAL_SERVER_ERROR),
    
    RESOURCE_NOT_FOUND("C404", "Resource Not Found", NOT_FOUND),
    
    FIELD_ERROR("C843", "잘못된 요청 값", BAD_REQUEST);
    
    private final String     code;
    private final String     baseMessage;
    private final HttpStatus httpStatus;
}
