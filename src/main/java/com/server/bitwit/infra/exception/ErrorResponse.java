package com.server.bitwit.infra.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
public class ErrorResponse
{
    private String code;
    
    private String message;
    
    private HttpStatus httpStatus;
    
    private Instant timestamp = Instant.now( );
    
    public static ErrorResponse createErrorResponse(BitwitException e)
    {
        var response = new ErrorResponse( );
        response.code       = e.getErrorCode( ).getCode( );
        response.message    = e.getMessage( );
        response.httpStatus = e.getErrorCode( ).getHttpStatus( );
        return response;
    }
    
    public static ResponseEntity<ErrorResponse> createErrorResponseEntity(BitwitException e)
    {
        var errorResponse = createErrorResponse(e);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
}
