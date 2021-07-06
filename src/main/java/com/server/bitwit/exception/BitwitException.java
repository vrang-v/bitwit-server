package com.server.bitwit.exception;

import lombok.Getter;

@Getter
public abstract class BitwitException extends RuntimeException
{
    private final ErrorCode errorCode;
    
    protected BitwitException(ErrorCode errorCode)
    {
        super(errorCode.getBaseMessage( ));
        this.errorCode = errorCode;
    }
    
    protected BitwitException(ErrorCode errorCode, String detailMessage)
    {
        super(errorCode.getBaseMessage( ) + ": " + detailMessage);
        this.errorCode = errorCode;
    }
}
