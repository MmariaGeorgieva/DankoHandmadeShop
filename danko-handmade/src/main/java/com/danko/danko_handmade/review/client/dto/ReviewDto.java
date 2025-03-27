package com.danko.danko_handmade.review.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDto {

    @NotBlank
    private String productCode;

    @NotBlank
    private String mainPhotoUrl;

    @NotNull
    private int rating;

    @NotBlank
    private String reviewText;
}
