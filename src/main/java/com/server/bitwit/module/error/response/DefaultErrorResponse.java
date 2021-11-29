package com.server.bitwit.module.error.response;

import com.server.bitwit.module.error.exception.BitwitException;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class DefaultErrorResponse implements BitwitErrorResponse {
    
    String     code;
    String     message;
    HttpStatus httpStatus;
    Instant    timestamp = Instant.now( );
    
    public static BitwitErrorResponse createErrorResponse(BitwitException e) {
        var response = new DefaultErrorResponse( );
        response.code       = e.getErrorCode( ).getCode( );
        response.message    = e.getMessage( );
        response.httpStatus = e.getErrorCode( ).getHttpStatus( );
        return response;
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e) {
        var errorResponse = createErrorResponse(e);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
}
