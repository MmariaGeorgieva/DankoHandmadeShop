package com.danko.danko_handmade.review.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
