package com.server.bitwit.module.error.exception;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

import static com.server.bitwit.module.error.exception.ErrorCode.FIELD_ERROR;

@Getter
public class FieldErrorException extends BitwitException {
    
    private static final long serialVersionUID = 23984L;
    
    private static final ErrorCode ERROR_CODE = FIELD_ERROR;
    
    private final List<ObjectError> objectErrors;
    
    public FieldErrorException(Errors errors) {
        super(ERROR_CODE);
        this.objectErrors = errors.getAllErrors( );
    }
    
    public FieldErrorException(List<ObjectError> objectErrors) {
        super(ERROR_CODE);
        this.objectErrors = objectErrors;
    }
}
