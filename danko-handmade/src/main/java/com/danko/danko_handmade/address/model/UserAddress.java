package com.danko.danko_handmade.address.model;

import com.danko.danko_handmade.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserAddress {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Embedded
    private Address address;

}
