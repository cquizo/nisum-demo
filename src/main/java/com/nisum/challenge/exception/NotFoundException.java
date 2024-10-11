package com.nisum.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends CustomException {
    @Serial
    private static final long serialVersionUID = -7636074275359233707L;

    private String message;
}
