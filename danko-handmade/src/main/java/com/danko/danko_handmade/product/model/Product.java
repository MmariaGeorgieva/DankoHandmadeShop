package com.danko.danko_handmade.product.model;

import com.danko.danko_handmade.discount.model.Discount;
import com.danko.danko_handmade.productphoto.model.ProductPhoto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "productPhotos")
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

    @Transient
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    private String mainPhotoUrl;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductPhoto> productPhotos = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime addedOn;

    @ManyToMany
    @JoinTable(
            name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    private List<Discount> discounts = new ArrayList<>();

    @Column(nullable = false)
    private double weight;
}
