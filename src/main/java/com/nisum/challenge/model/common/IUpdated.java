package com.nisum.challenge.model.common;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface IUpdated {
    ZonedDateTime getUpdatedOn();

    void setUpdatedOn(ZonedDateTime updatedOn);

    UUID getUpdatedBy();

    void setUpdatedBy(UUID updatedBy);
}
