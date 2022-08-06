package com.server.bitwit.module.error.exception;

import lombok.Getter;

@Getter
public class BitwitException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public BitwitException( ) {
        super(ErrorCode.SERVER_ERROR.getBaseMessage( ));
        this.errorCode = ErrorCode.SERVER_ERROR;
    }
    
    public BitwitException(Throwable throwable) {
        super(ErrorCode.SERVER_ERROR.getBaseMessage( ), throwable);
        this.errorCode = ErrorCode.SERVER_ERROR;
    }
    
    public BitwitException(ErrorCode errorCode) {
        super(errorCode.getBaseMessage( ));
        this.errorCode = errorCode;
    }
    
    public BitwitException(String detailMessage) {
        super(ErrorCode.SERVER_ERROR.getBaseMessage( ) + " - " + detailMessage);
        this.errorCode = ErrorCode.SERVER_ERROR;
    }
    
    public BitwitException(String detailMessage, Throwable throwable) {
        super(ErrorCode.SERVER_ERROR.getBaseMessage( ) + " - " + detailMessage, throwable);
        this.errorCode = ErrorCode.SERVER_ERROR;
    }
    
    public BitwitException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getBaseMessage( ) + " - " + detailMessage);
        this.errorCode = errorCode;
    }
    
    public BitwitException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getBaseMessage( ), throwable);
        this.errorCode = errorCode;
    }
    
    public BitwitException(ErrorCode errorCode, String detailMessage, Throwable throwable) {
        super(errorCode.getBaseMessage( ) + " - " + detailMessage, throwable);
        this.errorCode = errorCode;
    }
}
