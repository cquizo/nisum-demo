package com.nisum.challenge.util;

import com.nisum.challenge.model.common.ICreated;
import com.nisum.challenge.model.common.IUpdated;

import java.time.ZonedDateTime;
import java.util.UUID;

public class EntityUtil {

    public static void setCreated(ICreated created) {
        created.setCreatedOn(ZonedDateTime.now());
        created.setCreatedBy(UUID.randomUUID()); //TODO: replace with the authenticated user id
    }

    public static void setUpdated(IUpdated updated) {
        updated.setUpdatedOn(ZonedDateTime.now());
        updated.setUpdatedBy(UUID.randomUUID()); //TODO: replace with the authenticated user id
    }
}
