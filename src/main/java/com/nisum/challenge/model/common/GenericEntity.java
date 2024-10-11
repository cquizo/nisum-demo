package com.nisum.challenge.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class GenericEntity extends Identity implements IVersion, ICreated, IUpdated {
    @Serial
    private static final long serialVersionUID = 4540727371724829502L;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;
    @Column(name = "created_on", nullable = false, updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updated_by")
    private UUID updatedBy;
    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;
    @Version
    private long version;

}
