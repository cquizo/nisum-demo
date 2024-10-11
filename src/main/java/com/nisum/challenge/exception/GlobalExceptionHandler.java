package com.nisum.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(". "));

        ValidationErrorResponse response = new ValidationErrorResponse(message.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}