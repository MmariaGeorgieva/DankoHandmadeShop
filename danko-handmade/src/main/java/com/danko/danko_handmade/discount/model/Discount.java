package com.danko.danko_handmade.discount.model;

import com.danko.danko_handmade.product.model.Product;
import jakarta.persistence.*;
        import lombok.*;

        import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "products")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(unique = true)
    private String couponCode;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    @Column(nullable = false)
    private boolean active;

    private BigDecimal minOrderValue;

    @Column(nullable = false)
    private boolean multipleUse;

    @ManyToMany
    private List<Product> products;
}
