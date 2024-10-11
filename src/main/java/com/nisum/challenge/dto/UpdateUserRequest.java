package com.nisum.challenge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class UpdateUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7637914773114842162L;

    @NotBlank
    private String name;
    @NotNull
    @NotEmpty
    @Valid
    private List<PhoneDTO> phones;
}
