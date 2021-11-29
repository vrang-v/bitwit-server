package com.server.bitwit.module.error.response;

import com.server.bitwit.module.common.dto.BitwitResponse;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.FieldErrorException;
import org.springframework.http.ResponseEntity;

public interface BitwitErrorResponse extends BitwitResponse {
    
    static BitwitErrorResponse createErrorResponse(BitwitException e) {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponseWithObjectErrors(fe, fe.getObjectErrors( ));
        }
        return DefaultErrorResponse.createErrorResponse(e);
    }
    
    static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e) {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponseEntityWithObjectErrors(fe, fe.getObjectErrors( ));
        }
        return DefaultErrorResponse.createErrorResponseEntity(e);
    }
}
