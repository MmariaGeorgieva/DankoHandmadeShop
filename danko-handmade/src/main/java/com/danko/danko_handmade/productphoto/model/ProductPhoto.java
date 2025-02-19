package com.danko.danko_handmade.productphoto.model;

import com.danko.danko_handmade.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "product")
public class ProductPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
