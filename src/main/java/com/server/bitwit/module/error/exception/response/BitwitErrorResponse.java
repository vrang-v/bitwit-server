package com.server.bitwit.module.error.exception.response;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.FieldErrorException;
import com.server.bitwit.module.common.dto.BitwitResponse;
import org.springframework.http.ResponseEntity;

public interface BitwitErrorResponse extends BitwitResponse {
    
    static BitwitErrorResponse createErrorResponse(BitwitException e) {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponse(fe, fe.getErrors( ).getFieldErrors( ));
        }
        return DefaultErrorResponse.createErrorResponse(e);
    }
    
    static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e) {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponseEntity(fe, fe.getErrors( ).getFieldErrors( ));
        }
        return DefaultErrorResponse.createErrorResponseEntity(e);
    }
}
