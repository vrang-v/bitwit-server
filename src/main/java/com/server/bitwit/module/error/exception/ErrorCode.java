package com.server.bitwit.module.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    SERVER_ERROR("C1000", "알 수 없는 오류가 발생했습니다.", INTERNAL_SERVER_ERROR),
    
    METHOD_NOT_SUPPOERTED("C4005", "지원하지 않는 요청 메소드입니다.", METHOD_NOT_ALLOWED),
    
    INVALID_REQUEST("C4000", "잘못된 요청이 존재합니다.", BAD_REQUEST),
    
    AUTHENTICATION_FAILED("C4001", "요청이 잘못되어 인증을 진행할 수 없습니다.", UNAUTHORIZED),
    
    RESOURCE_NOT_FOUND("C4004", "존재하지 않는 리소스가 요청되었습니다.", NOT_FOUND),
    
    NO_PERMISSION("C4003", "권한이 없어 요청이 거부되었습니다.", FORBIDDEN),
    
    FIELD_ERROR("C4000", "잘못된 요청 필드가 존재합니다.", BAD_REQUEST),
    
    REQUEST_LIMIT("C4029", "요청량이 많아 사용이 제한됩니다.", TOO_MANY_REQUESTS),
    
    FILE_NOT_FOUND("C4004", "요청된 파일이 존재하지 않습니다.", NOT_FOUND),
    
    EMPTY_REQUEST_BODY("C4010", "요청 본문이 비어있습니다.", BAD_REQUEST),
    
    INVALID_JWT("C4001", "잘못된 JWT 형식입니다.", BAD_REQUEST);
    
    private final String     code;
    private final String     baseMessage;
    private final HttpStatus httpStatus;
}
