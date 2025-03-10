package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.product.model.ProductSection;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class EditProductRequest {

    @Column(nullable = false)
    private String listingTitle;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ProductSection> productSection;

    @Column(nullable = false)
    private int stockQuantity;

    private String mainPhotoUrl;

    @ElementCollection
    private List<String> additionalPhotos;

    @Column(nullable = false)
    private double weight;
}
