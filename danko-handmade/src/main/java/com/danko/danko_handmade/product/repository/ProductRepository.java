package com.danko.danko_handmade.product.repository;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByActive(boolean active);

    @Query("SELECT p FROM Product p WHERE p.productSection = :section AND p.id != :productId ORDER BY RAND()")
    List<Product> findSixRandomProductsFromTheSameSection(@Param("section") ProductSection section, @Param("productId") UUID productId, Pageable pageable);

}
