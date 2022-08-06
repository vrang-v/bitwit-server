package com.server.bitwit.module.error.response;

import com.server.bitwit.module.error.exception.BitwitException;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class FieldErrorResponse implements BitwitErrorResponse {
    
    String              code;
    String              message;
    HttpStatus          httpStatus;
    Instant             timestamp = Instant.now( );
    List<FieldErrorDto> fieldErrors;
    
    public static BitwitErrorResponse createErrorResponse(BitwitException e, Collection<FieldError> fieldErrors) {
        var response = new FieldErrorResponse( );
        response.code        = e.getErrorCode( ).getCode( );
        response.message     = e.getMessage( );
        response.httpStatus  = e.getErrorCode( ).getHttpStatus( );
        response.fieldErrors = fieldErrors.stream( )
                                          .map(FieldErrorDto::new)
                                          .toList( );
        return response;
    }
    
    public static BitwitErrorResponse createErrorResponseWithObjectErrors(BitwitException e, Collection<ObjectError> objectErrors) {
        var response = new FieldErrorResponse( );
        response.code        = e.getErrorCode( ).getCode( );
        response.message     = e.getMessage( );
        response.httpStatus  = e.getErrorCode( ).getHttpStatus( );
        response.fieldErrors = objectErrors.stream( )
                                           .map(FieldErrorDto::new)
                                           .toList( );
        return response;
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntity(BitwitException e, Collection<FieldError> fieldErrors) {
        var errorResponse = createErrorResponse(e, fieldErrors);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
    
    public static ResponseEntity<BitwitErrorResponse> createErrorResponseEntityWithObjectErrors(BitwitException e, Collection<ObjectError> objectErrors) {
        var errorResponse = createErrorResponseWithObjectErrors(e, objectErrors);
        return ResponseEntity.status(e.getErrorCode( ).getHttpStatus( ))
                             .body(errorResponse);
    }
    
    @Value
    static class FieldErrorDto {
        
        String code;
        String field;
        String message;
        
        public FieldErrorDto(FieldError fieldError) {
            this.code    = fieldError.getCode( );
            this.message = fieldError.getDefaultMessage( );
            this.field   = fieldError.getField( );
        }
        
        public FieldErrorDto(ObjectError objectError) {
            this.code    = objectError.getCode( );
            this.message = objectError.getDefaultMessage( );
            if (objectError instanceof FieldError fieldError) {
                this.field = fieldError.getField( );
            }
            else {
                this.field = null;
            }
        }
    }
}
