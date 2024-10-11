package com.nisum.challenge.model.common;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ICreated {
    ZonedDateTime getCreatedOn();

    void setCreatedOn(ZonedDateTime createdOn);

    UUID getCreatedBy();

    void setCreatedBy(UUID createdBy);
}
