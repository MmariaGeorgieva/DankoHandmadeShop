package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.review.client.dto.LeaveReview;
import com.danko.danko_handmade.review.service.ReviewService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.review.client.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final UserService userService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @Autowired
    public ReviewController(UserService userService, ProductService productService, ReviewService reviewService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.reviewService = reviewService;
        this.orderService = orderService;
    }

    @GetMapping("/new-review/{orderId}/{productId}")
    public ModelAndView getLeaveReviewPage(@PathVariable UUID orderId,
                                           @PathVariable UUID productId,
                                           Authentication userAuthentication) {
        ModelAndView modelAndView = new ModelAndView();
        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID userId = userData.getUserId();
            User user = userService.getById(userId);
            Product product = productService.getProductById(productId);
            Order order = orderService.getOrderById(orderId);


            modelAndView.setViewName("new-review");
            modelAndView.addObject("user", user);
            modelAndView.addObject("product", product);
            modelAndView.addObject("order", order);
            modelAndView.addObject("leaveReview", new LeaveReview());
        }
        return modelAndView;
    }

    @PostMapping("/new-review")
    public String submitReview(@RequestParam("productCode") String productCode,
                               @RequestParam("userId") UUID userId,
                               @RequestParam("orderId") UUID orderId,
                               @RequestParam("rating") int rating,
                               @RequestParam("textReview") String textReview,
                               @RequestParam("mainPhotoUrl") String mainPhotoUrl,
                               Authentication userAuthentication) {

        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID currentUserId = userData.getUserId();

            if (!currentUserId.equals(userId)) {
                return "redirect:/not-found";
            }

            reviewService.leaveReview(currentUserId, productCode, orderId, textReview, rating, mainPhotoUrl);
            return "redirect:/reviews/public-reviews";
        }
        return "redirect:/login";
    }

    @GetMapping("/public-reviews")
    public ModelAndView getAllReviews(Authentication userAuthentication) {
        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID currentUserId = userData.getUserId();
            User user = userService.getById(currentUserId);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("public-reviews");
            modelAndView.addObject("user", user);
            modelAndView.addObject("authorities", userData.getAuthorities());

            List<ReviewDto> allReviews = reviewService.getAllReviews();
            modelAndView.addObject("allReviews", allReviews);
            return modelAndView;
        }
        return new ModelAndView("redirect:/login");
    }

}
