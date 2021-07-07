package com.server.bitwit.controller.advice;

import com.server.bitwit.exception.BitwitException;
import com.server.bitwit.exception.ErrorResponse;
import com.server.bitwit.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.server.bitwit.exception.ErrorResponse.createErrorResponseEntity;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice
{
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e)
    {
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBitwitException(BitwitException e)
    {
        return createErrorResponseEntity(e);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        return createErrorResponseEntity(new BitwitException(e.getMessage( )));
    }
}
