package com.danko.danko_handmade.Address.model;

import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.user.model.RegisteredUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
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

    @ManyToOne(fetch = FetchType.EAGER)
    private RegisteredUser registeredUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

}
