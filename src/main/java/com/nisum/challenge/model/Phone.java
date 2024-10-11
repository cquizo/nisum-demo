package com.nisum.challenge.model;

import com.nisum.challenge.model.common.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity(name = "phones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Phone extends GenericEntity {
    @Serial
    private static final long serialVersionUID = 1350161298809457904L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String number;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "country_code")
    private String countryCode;
}