package com.nisum.challenge.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1947554836011194893L;

    private UUID id;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private ZonedDateTime lastLogin;
    private String token;
    private boolean isActive;

}
