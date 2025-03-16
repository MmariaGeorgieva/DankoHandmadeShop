package com.danko.danko_handmade.address.model;

import com.danko.danko_handmade.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String recipientName;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String streetNumber;

    private String phoneNumber;

    @ManyToOne
    private User user;
}
