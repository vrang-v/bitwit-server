package com.server.bitwit.module.error.exception.response;

import com.server.bitwit.module.error.exception.BitwitException;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class FieldErrorResponse implements BitwitErrorResponse {
    
    String           code;
    String           message;
    HttpStatus       httpStatus;
    Instant          timestamp = Instant.now( );
    List<FieldError> fieldErrors;

    public static BitwitErrorResponse createErrorResponse(BitwitException e, Collection<FieldError> fieldErrors) {
        var response = new FieldErrorResponse( );
        response.code        = e.getErrorCode( ).getCode( );
        response.message     = e.getMessage( );
        response.httpStatus  = e.getErrorCode( ).getHttpStatus( );
        response.fieldErrors = new ArrayList<>(fieldErrors);
        return response;
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e,
                                                                                Collection<FieldError> fieldErrors) {
        var errorResponse = createErrorResponse(e, fieldErrors);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
}
