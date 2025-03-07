package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EditProductsPageRequest {

    private List<ProductEditRequest> products;

    @Data
    public static class ProductEditRequest {
        private Long id;

        @Min(value = 0, message = "Stock quantity must be 0 or greater")
        private int stockQuantity;

        @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
        private BigDecimal price;

        private String mainImageUrl;

        private String listingTitle;
    }
}
