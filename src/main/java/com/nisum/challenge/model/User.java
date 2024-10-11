package com.nisum.challenge.model;

import com.nisum.challenge.model.common.GenericEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.List;

@Entity(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends GenericEntity {

    @Serial
    private static final long serialVersionUID = 7298806371922789344L;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    @Column(name = "last_login")
    private ZonedDateTime lastLogin;

    private String token;

    @Column(name = "is_active")
    private boolean isActive;
}