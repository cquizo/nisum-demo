package com.nisum.challenge.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5787677443410555250L;

    private UUID id;

    private String name;
    private String email;
    private String password;

    private List<PhoneDTO> phones;

    private ZonedDateTime createdOn;
    private ZonedDateTime modifiedOn;
    private ZonedDateTime lastLogin;

    private String token;
    private boolean isActive;

}