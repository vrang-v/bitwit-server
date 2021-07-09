package com.server.bitwit.infra.exception.response;

import com.server.bitwit.infra.exception.BitwitException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
public class DefaultErrorResponse implements BitwitErrorResponse
{
    private String code;
    
    private String message;
    
    private HttpStatus httpStatus;
    
    private Instant timestamp = Instant.now( );
    
    public static BitwitErrorResponse createErrorResponse(BitwitException e)
    {
        var response = new DefaultErrorResponse( );
        response.code       = e.getErrorCode( ).getCode( );
        response.message    = e.getMessage( );
        response.httpStatus = e.getErrorCode( ).getHttpStatus( );
        return response;
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e)
    {
        var errorResponse = createErrorResponse(e);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
}
