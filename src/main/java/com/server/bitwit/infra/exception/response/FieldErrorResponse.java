package com.server.bitwit.infra.exception.response;

import com.server.bitwit.infra.exception.BitwitException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class FieldErrorResponse implements BitwitErrorResponse
{
    private String code;
    
    private String message;
    
    private HttpStatus httpStatus;
    
    private Instant timestamp = Instant.now( );
    
    private List<FieldError> fieldErrors;
    
    public static BitwitErrorResponse createErrorResponse(BitwitException e, Collection<FieldError> fieldErrors)
    {
        var response = new FieldErrorResponse( );
        response.code        = e.getErrorCode( ).getCode( );
        response.message     = e.getMessage( );
        response.httpStatus  = e.getErrorCode( ).getHttpStatus( );
        response.fieldErrors = new ArrayList<>(fieldErrors);
        return response;
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e,
                                                                                Collection<FieldError> fieldErrors)
    {
        var errorResponse = createErrorResponse(e, fieldErrors);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
}
