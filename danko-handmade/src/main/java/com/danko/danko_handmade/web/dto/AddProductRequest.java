package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.product.model.ProductSection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {

    private String productCode;

    @NotBlank(message = "Listing title cannot be empty")
    private String listingTitle;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Product section is required")
    private ProductSection productSection;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    private MultipartFile mainPhoto;

    private List<MultipartFile> additionalPhotos;

}
