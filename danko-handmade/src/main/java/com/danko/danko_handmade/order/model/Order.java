package com.danko.danko_handmade.order.model;

import com.danko.danko_handmade.Address.model.Address;
import com.danko.danko_handmade.orderItem.model.OrderItem;
import com.danko.danko_handmade.user.model.GuestUser;
import com.danko.danko_handmade.user.model.RegisteredUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private RegisteredUser registeredUser;

    @Column(nullable = false)
    private LocalDateTime orderedOn;

    @Column(nullable = false)
    private LocalDateTime shipBy;

    @Column(nullable = false)
    private LocalDateTime shippedOn;

    private String adminNote;

    @ManyToOne
    private GuestUser guestUser;

    @OneToOne(fetch = FetchType.EAGER)
    private Address deliveryAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderedItems = new ArrayList<>();

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Message> orderMessages = new ArrayList<>();
}
