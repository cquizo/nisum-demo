package com.nisum.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
}