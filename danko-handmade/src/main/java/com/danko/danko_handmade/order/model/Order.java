package com.danko.danko_handmade.order.model;

import com.danko.danko_handmade.address.model.OrderDeliveryAddress;
import com.danko.danko_handmade.message.model.Message;
import com.danko.danko_handmade.orderedItem.model.OrderedItem;
import com.danko.danko_handmade.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(nullable = true, length = 500)
    private String noteToSeller;

    @Column(nullable = false)
    private LocalDateTime orderedOn;

    @Column(nullable = false)
    private LocalDateTime shipBy;

    private LocalDateTime shippedOn;

    private String adminNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.EAGER)
    private OrderDeliveryAddress address;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderedItem> orderedItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Message> orderMessages = new ArrayList<>();
}
