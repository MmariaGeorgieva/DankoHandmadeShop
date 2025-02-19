package com.danko.danko_handmade.cart.model;

import com.danko.danko_handmade.cartitem.model.CartItem;
import com.danko.danko_handmade.discount.model.Discount;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive = true;

    private String couponCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @Transient
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Transient
    private BigDecimal totalAmount;

}
