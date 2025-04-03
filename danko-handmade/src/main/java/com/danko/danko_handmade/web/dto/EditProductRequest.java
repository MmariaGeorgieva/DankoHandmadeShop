package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.product.model.ProductSection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProductRequest {

    @NotBlank(message = "Listing title cannot be blank")
    private String listingTitle;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be blank")
    @Positive(message = "Price cannot be a negative number")
    private BigDecimal price;

    @NotNull
    private ProductSection productSection;

    @NotNull
    @Min(0)
    private int stockQuantity;

    @NotNull
    private boolean active;






}
