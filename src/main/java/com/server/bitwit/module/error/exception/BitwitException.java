package com.server.bitwit.module.error.exception;

import lombok.Getter;

@Getter
public class BitwitException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public BitwitException( ) {
        super(ErrorCode.DEFAULT.getBaseMessage( ));
        this.errorCode = ErrorCode.DEFAULT;
    }
    
    public BitwitException(Throwable throwable) {
        super(ErrorCode.DEFAULT.getBaseMessage( ), throwable);
        this.errorCode = ErrorCode.DEFAULT;
    }
    
    public BitwitException(ErrorCode errorCode) {
        super(errorCode.getBaseMessage( ));
        this.errorCode = errorCode;
    }
    
    public BitwitException(String detailMessage) {
        super(ErrorCode.DEFAULT.getBaseMessage( ) + " - " + detailMessage);
        this.errorCode = ErrorCode.DEFAULT;
    }
    
    public BitwitException(String detailMessage, Throwable throwable) {
        super(ErrorCode.DEFAULT.getBaseMessage( ) + " - " + detailMessage, throwable);
        this.errorCode = ErrorCode.DEFAULT;
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
