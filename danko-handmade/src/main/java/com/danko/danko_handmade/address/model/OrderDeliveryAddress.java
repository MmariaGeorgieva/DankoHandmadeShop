package com.danko.danko_handmade.address.model;

import com.danko.danko_handmade.order.model.Order;
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
public class OrderDeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "address")
    private Order order;

    @Embedded
    private Address address;

}
