package com.danko.danko_handmade.address.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
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
}
