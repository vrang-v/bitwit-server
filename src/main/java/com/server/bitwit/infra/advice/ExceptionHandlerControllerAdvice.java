package com.server.bitwit.infra.advice;

import com.server.bitwit.infra.exception.response.BitwitErrorResponse;
import com.server.bitwit.infra.exception.BitwitException;
import com.server.bitwit.infra.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.server.bitwit.infra.exception.response.BitwitErrorResponse.createErrorResponseEntity;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice
{
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleNotFoundException(NotFoundException e)
    {
        log.error("handleNotFoundException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error("handleMethodArgumentNotValidException", e);
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleBitwitException(BitwitException e)
    {
        log.error("handleBitwitException", e);
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<BitwitErrorResponse> handleException(Exception e)
    {
        log.error("handleException", e);
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
}
