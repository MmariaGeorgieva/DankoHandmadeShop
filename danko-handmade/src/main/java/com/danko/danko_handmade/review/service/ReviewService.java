package com.danko.danko_handmade.review.service;

import com.danko.danko_handmade.review.client.ReviewClient;
import com.danko.danko_handmade.review.client.dto.UpsertReview;
import com.danko.danko_handmade.review.client.dto.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReviewService {

    private final ReviewClient reviewClient;

    @Autowired
    public ReviewService(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;
    }

    public void upsertReview(UUID userId, UUID productId, UUID orderId, String textReview,
                            int rating, String mainPhotoUrl) {

        UpsertReview review = UpsertReview.builder()
                .userId(userId)
                .productId(productId)
                .orderId(orderId)
                .mainPhotoUrl(mainPhotoUrl)
                .textReview(textReview)
                .rating(rating)
                .createdOn(LocalDateTime.now())
                .build();

        ResponseEntity<Void> httpResponse = reviewClient.upsertReview(review);
        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            log.error("[Feign call to review-svc failed] Cannot leave review");
        }
    }

    public List<ReviewDto> getAllReviews() {
        ResponseEntity<List<ReviewDto>> response = reviewClient.getAllReviews();
        return response.getBody();
    }
}
