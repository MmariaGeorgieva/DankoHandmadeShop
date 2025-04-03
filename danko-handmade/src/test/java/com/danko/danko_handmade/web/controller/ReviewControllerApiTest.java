package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.review.client.dto.ReviewDto;
import com.danko.danko_handmade.review.service.ReviewService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
public class ReviewControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private ReviewService reviewService;
    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private View view;

    @Test
    void getRequestToAllReviewsEndpoint_whenUserIsAuthenticated_shouldReturnPublicReviewsView() throws Exception {

        List<ReviewDto> mockReviews = List.of(
                new ReviewDto(UUID.randomUUID(), "photo1.jpg", 5, "Review 1"),
                new ReviewDto(UUID.randomUUID(), "photo2.jpg", 4, "Review 2")
        );

        UUID mockUserId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");

        AuthenticationMetadata mockAuthMetadata = new AuthenticationMetadata(
                mockUserId, "testUser", "testPassword", Role.USER);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockAuthMetadata, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.getById(mockUserId)).thenReturn(mockUser);
        when(reviewService.getAllReviews()).thenReturn(mockReviews);

        MockHttpServletRequestBuilder request = get("/reviews/public-reviews")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("public-reviews"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("authorities"))
                .andExpect(model().attributeExists("allReviews"));
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUpsertReviewPage_authenticated() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        Product product = new Product();
        Order order = new Order();

        AuthenticationMetadata userData = new AuthenticationMetadata(userId, "testUser", "testPassword", Role.USER);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userData, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.getById(userId)).thenReturn(user);
        when(productService.getProductById(productId)).thenReturn(product);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/reviews/new-review/{orderId}/{productId}", orderId, productId)
                        .param("message", "Test message")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new-review"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("upsertReview"))
                .andExpect(model().attribute("message", "Test message"));

        SecurityContextHolder.clearContext();
    }

    @Test
    void testUpsertReview_authenticated_and_allowed() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User mockUser = new User();
        mockUser.setId(userId);

        AuthenticationMetadata userData = new AuthenticationMetadata(userId, "testUser", "testPassword", Role.USER);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userData, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        Order mockOrder = new Order();
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);
        when(orderService.getAllOrdersByUserIdNewestFirst(userId)).thenReturn(List.of(mockOrder));

        mockMvc.perform(post("/reviews/new-review")
                        .param("productId", productId.toString())
                        .param("userId", userId.toString())
                        .param("orderId", orderId.toString())
                        .param("rating", "5")
                        .param("textReview", "Great product!")
                        .param("mainPhotoUrl", "photo.jpg")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())  // Очакваме редирект
                .andExpect(redirectedUrl("/reviews/public-reviews"));  // Проверка за правилен редирект


        verify(reviewService, times(1)).upsertReview(userId, productId, orderId, "Great product!", 5, "photo.jpg");
        SecurityContextHolder.clearContext();
    }
}
