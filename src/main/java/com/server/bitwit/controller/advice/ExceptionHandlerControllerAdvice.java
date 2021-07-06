package com.server.bitwit.controller.advice;

import com.server.bitwit.exception.ErrorResponse;
import com.server.bitwit.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
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
}
