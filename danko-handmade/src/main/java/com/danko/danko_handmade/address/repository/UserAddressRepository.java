package com.danko.danko_handmade.address.repository;

import com.danko.danko_handmade.address.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
}
