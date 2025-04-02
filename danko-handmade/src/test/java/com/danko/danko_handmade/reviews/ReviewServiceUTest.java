package com.danko.danko_handmade.reviews;

import com.danko.danko_handmade.review.client.ReviewClient;
import com.danko.danko_handmade.review.client.dto.ReviewDto;
import com.danko.danko_handmade.review.client.dto.UpsertReview;
import com.danko.danko_handmade.review.service.ReviewService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUTest {

    @Mock
    private ReviewClient reviewClient;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void when_GetAllReviews_ShouldReturnListOfReviews() {

        List<ReviewDto> mockReviews = new ArrayList<>();
        ReviewDto review1 = ReviewDto.builder()
                .productId(UUID.randomUUID())
                .reviewText("Review Text 1")
                .rating(5)
                .build();

        ReviewDto review2 = ReviewDto.builder()
                .productId(UUID.randomUUID())
                .reviewText("Review Text 2")
                .rating(3)
                .build();

        mockReviews.add(review1);
        mockReviews.add(review2);

        ResponseEntity<List<ReviewDto>> responseEntity = ResponseEntity.ok(mockReviews);
        when(reviewClient.getAllReviews()).thenReturn(responseEntity);

        List<ReviewDto> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Review Text 1", result.get(0).getReviewText());
        assertEquals(5, result.get(0).getRating());
        assertEquals("Review Text 2", result.get(1).getReviewText());
        assertEquals(3, result.get(1).getRating());
        verify(reviewClient, times(1)).getAllReviews();
    }

    @Test
    void when_upsertReview_ShouldCallReviewClient() {

        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String textReview = "Great product!";
        int rating = 5;
        String mainPhotoUrl = "http://example.com/photo.jpg";

        ResponseEntity<Void> successResponse = ResponseEntity.ok().build();
        when(reviewClient.upsertReview(Mockito.any(UpsertReview.class))).thenReturn(successResponse);

        reviewService.upsertReview(userId, productId, orderId, textReview, rating, mainPhotoUrl);

        verify(reviewClient, Mockito.times(1)).upsertReview(Mockito.any(UpsertReview.class));
    }

    @Test
    void when_upsertReview_ShouldLogErrorOnFailure() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String textReview = "Bad experience!";
        int rating = 1;
        String mainPhotoUrl = "http://example.com/bad-photo.jpg";

        ResponseEntity<Void> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        Mockito.when(reviewClient.upsertReview(Mockito.any(UpsertReview.class))).thenReturn(errorResponse);

        reviewService.upsertReview(userId, productId, orderId, textReview, rating, mainPhotoUrl);

        Mockito.verify(reviewClient, Mockito.times(1)).upsertReview(Mockito.any(UpsertReview.class));
    }
}
