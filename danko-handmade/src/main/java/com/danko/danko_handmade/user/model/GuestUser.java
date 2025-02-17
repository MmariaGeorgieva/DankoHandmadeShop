package com.danko.danko_handmade.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


}
