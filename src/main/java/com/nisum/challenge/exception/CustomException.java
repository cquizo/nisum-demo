package com.nisum.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomException extends Exception {
    @Serial
    private static final long serialVersionUID = -7636074275359233707L;

    private String message;

}
