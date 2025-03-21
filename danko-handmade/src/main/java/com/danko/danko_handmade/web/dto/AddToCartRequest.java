package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class AddToCartRequest {

    private UUID userId;

    @NotBlank
    private UUID productId;

    @NotBlank
    private int quantity;

}
