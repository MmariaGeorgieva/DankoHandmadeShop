package com.danko.danko_handmade.product.model;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String listingTitle;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<ProductSection> productSection = new ArrayList<>();

    @Column(nullable = false)
    private int stockQuantity;

    @Transient
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    private String mainPhotoUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> additionalPhotosUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime addedOn;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private boolean active;
}
