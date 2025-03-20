package com.danko.danko_handmade.email.repository;

import com.danko.danko_handmade.email.model.ContactFormEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactFormEmailRepository extends JpaRepository<ContactFormEmail, UUID> {
}
