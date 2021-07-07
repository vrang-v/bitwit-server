package com.server.bitwit.infra.advice;

import com.server.bitwit.infra.exception.BitwitException;
import com.server.bitwit.infra.exception.ErrorResponse;
import com.server.bitwit.infra.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.server.bitwit.infra.exception.ErrorResponse.createErrorResponseEntity;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice
{
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e)
    {
        log.error("handleNotFoundException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error("handleMethodArgumentNotValidException", e);
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBitwitException(BitwitException e)
    {
        log.error("handleBitwitException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        log.error("handleException", e);
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
}
