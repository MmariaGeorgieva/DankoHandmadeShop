package com.danko.danko_handmade.review.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewDto {

    @NotNull
    private UUID productId;

    @NotBlank
    private String mainPhotoUrl;

    @NotNull
    private int rating;

    @NotBlank
    private String reviewText;
}
