package com.server.bitwit.module.error.exception;

public class NotFoundException extends BitwitException
{
    private static final ErrorCode ERROR_CODE = ErrorCode.RESOURCE_NOT_FOUND;
    
    public NotFoundException( )
    {
        super(ERROR_CODE);
    }
    
    public NotFoundException(String detailMessage)
    {
        super(ERROR_CODE, detailMessage);
    }
}
