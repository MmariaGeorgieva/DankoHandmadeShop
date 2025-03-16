package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.product.model.ProductSection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProductRequest {

    @NotNull
    private String listingTitle;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private ProductSection productSection;

    @NotNull
    @Min(0)
    private int stockQuantity;

    @NotNull
    private boolean active;






}
