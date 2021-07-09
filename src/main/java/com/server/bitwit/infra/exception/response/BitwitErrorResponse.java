package com.server.bitwit.infra.exception.response;

import com.server.bitwit.infra.exception.BitwitException;
import com.server.bitwit.infra.exception.FieldErrorException;
import com.server.bitwit.module.common.BitwitResponse;
import org.springframework.http.ResponseEntity;

public interface BitwitErrorResponse extends BitwitResponse
{
    static BitwitErrorResponse createErrorResponse(BitwitException e)
    {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponse(fe, fe.getErrors( ).getFieldErrors( ));
        }
        return DefaultErrorResponse.createErrorResponse(e);
    }
    
    static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e)
    {
        if (FieldErrorException.class.isAssignableFrom(e.getClass( ))) {
            var fe = (FieldErrorException)e;
            return FieldErrorResponse.createErrorResponseEntity(fe, fe.getErrors( ).getFieldErrors( ));
        }
        return DefaultErrorResponse.createErrorResponseEntity(e);
    }
}
