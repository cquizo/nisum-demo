package com.nisum.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PhoneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3949232562593469193L;

    @NotBlank
    private String number;
    @NotBlank
    private String citycode;
    @NotBlank
    private String countrycode;
}
