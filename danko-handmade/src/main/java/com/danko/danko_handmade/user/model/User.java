package com.danko.danko_handmade.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime registeredOn;

    private boolean subscribedToBulletin;

    private String recipientName;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String city;

    private String postalCode;

    private String street;

    private String streetNumber;

    private String phoneNumber;

}
