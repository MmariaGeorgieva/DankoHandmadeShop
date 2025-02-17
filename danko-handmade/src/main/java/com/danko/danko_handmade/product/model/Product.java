package com.danko.danko_handmade.product.model;

import com.danko.danko_handmade.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String listingTitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductSection productSection;

    @Column(nullable = false)
    private int stockQuantity;

    private boolean inStock;

    private String mainPhotoUrl;

//    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
//    private List<ProductPhoto> productPhotos = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime addedOn;

//    @OneToOne
//    @Column(nullable = true)
//    private Discount discount;

    @Column(nullable = false)
    private double weight;
}
