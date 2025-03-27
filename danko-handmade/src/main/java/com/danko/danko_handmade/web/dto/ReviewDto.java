package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDto {

    @NotBlank
    private String productCode;

    @NotNull
    private int rating;

    @NotBlank
    private String reviewText;
}
