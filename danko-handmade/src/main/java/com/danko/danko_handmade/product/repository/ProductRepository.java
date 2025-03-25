package com.danko.danko_handmade.product.repository;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByActive(boolean active);

    @Query("SELECT p FROM Product p ORDER BY RAND()")
    List<Product> findSixRandomProducts(Pageable pageable);

    List<Product> findAllByActiveAndProductSection(boolean active, ProductSection section);

    List<Product> findAllByOrderByAddedOnDesc();

    List<Product> findAllByOrderByItemsSoldDesc();

    Product findTopByOrderByAddedOnDesc();
}
