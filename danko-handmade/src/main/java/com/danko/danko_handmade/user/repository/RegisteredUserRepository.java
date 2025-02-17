package com.danko.danko_handmade.user.repository;

import com.danko.danko_handmade.user.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, UUID> {
}
