package com.danko.danko_handmade.cartitem.model;

import com.danko.danko_handmade.cart.model.Cart;
import com.danko.danko_handmade.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private BigDecimal priceAtAdd;

    private int discountAtAdd;

    @Enumerated(EnumType.STRING)
    private CartItemStatus status;
}
