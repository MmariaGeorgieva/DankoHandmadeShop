package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class AddToCartRequest {

    private UUID userId;

    @NotBlank
    private UUID productId;

    @NotBlank
    private int quantity;

}
