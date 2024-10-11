package com.nisum.challenge.repository;

import com.nisum.challenge.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

}
