package com.server.bitwit.module.error.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class FieldErrorException extends BitwitException
{
    private static final long serialVersionUID = 23984L;
    
    private static final ErrorCode ERROR_CODE = ErrorCode.FIELD_ERROR;
    
    private final Errors errors;
    
    public FieldErrorException(Errors errors)
    {
        super(ERROR_CODE);
        this.errors = errors;
    }
}
