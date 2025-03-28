package com.danko.danko_handmade.review.service;

import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.review.client.ReviewClient;
import com.danko.danko_handmade.review.client.dto.LeaveReview;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.review.client.dto.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReviewService {

    private final ReviewClient reviewClient;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public ReviewService(ReviewClient reviewClient, OrderService orderService, UserService userService, ProductService productService) {
        this.reviewClient = reviewClient;
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    public void leaveReview(UUID userId, UUID productId, UUID orderId, String textReview,
                            int rating, String mainPhotoUrl) {

        List<Order> userOrders = orderService.getAllOrdersByUserIdNewestFirst(userId);
        Order order = orderService.getOrderById(orderId);
        if(!userOrders.contains(order)) {
            throw new RuntimeException("You are not allowed to leave Review for this order");
        }

        LeaveReview review = LeaveReview.builder()
                .userId(userId)
                .productId(productId)
                .mainPhotoUrl(mainPhotoUrl)
                .textReview(textReview)
                .rating(rating)
                .createdOn(LocalDateTime.now())
                .build();

        ResponseEntity<Void> httpResponse = reviewClient.leaveReview(review);
        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            log.error("[Feign call to review-svc failed] Cannot leave review");
        }
    }

    public List<ReviewDto> getAllReviews() {
        return reviewClient.getAllReviews();
    }
}
