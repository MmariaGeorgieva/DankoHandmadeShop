package com.danko.danko_handmade.orderedItem.model;

import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"order", "product"})
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private int quantity;

    private int discountAtPurchase;

    private BigDecimal priceAtPurchase;
}
