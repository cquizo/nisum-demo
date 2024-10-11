package com.nisum.challenge.model.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Data
public class Identity implements Serializable, IIdentity {
    @Serial
    private static final long serialVersionUID = 7625459612985664755L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

}