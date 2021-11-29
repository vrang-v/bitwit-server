package com.server.bitwit.module.error.exception;

public class InvalidRequestException extends BitwitException {
    
    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_REQUEST;
    
    public InvalidRequestException(String detailMessage) {
        super(ERROR_CODE, detailMessage);
    }
}
