package com.server.bitwit.module.error;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.error.exception.FieldErrorException;
import com.server.bitwit.module.error.exception.NotFoundException;
import com.server.bitwit.module.error.response.BitwitErrorResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.server.bitwit.module.error.exception.ErrorCode.SERVER_ERROR;
import static com.server.bitwit.module.error.response.BitwitErrorResponse.createErrorResponseEntity;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleBitwitException(BitwitException e) {
        log.error("handleBitwitException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("handleNotFoundException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(new BitwitException(ErrorCode.METHOD_NOT_SUPPOERTED));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);
        return createErrorResponseEntity(new BitwitException(ErrorCode.EMPTY_REQUEST_BODY));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.warn("handleAuthenticationException {}", e.getMessage( ));
        return createErrorResponseEntity(new BitwitException(ErrorCode.AUTHENTICATION_FAILED));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleJwtException(JwtException e) {
        log.warn("handleJwtException {}", e.getMessage( ));
        return createErrorResponseEntity(new BitwitException(ErrorCode.AUTHENTICATION_FAILED));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        return createErrorResponseEntity(new FieldErrorException(e));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleConversionFailedException(ConversionFailedException e) {
        log.error("handleConversionFailedException", e);
        var cause = e.getCause( );
        return createErrorResponseEntity(cause instanceof BitwitException be ? be : new BitwitException(e));
    }
    
    @Order
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        return createErrorResponseEntity(new BitwitException(SERVER_ERROR));
    }
}
