package com.danko.danko_handmade.review.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpsertReview {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID productId;

    @NotNull
    private String mainPhotoUrl;

    @NotNull
    @NotBlank
    @Size(max = 1000)
    private String textReview;

    @NotNull
    private int rating;

    @NotNull
    private LocalDateTime createdOn;
}
