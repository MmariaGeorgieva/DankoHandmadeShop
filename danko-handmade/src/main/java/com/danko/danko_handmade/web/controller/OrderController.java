package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderController(UserService userService, OrderService orderService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping("/complete-order")
    public String completeOrder(HttpSession session, Authentication userAuthentication) {
        Map<Product, Integer> cartContent = (Map<Product, Integer>) session.getAttribute("cartContent");

        if (cartContent == null || cartContent.isEmpty()) {
            return "redirect:/cart";
        }
        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID userId = userData.getUserId();
            User user = userService.getById(userId);

//            orderService.createOrder(user);

            productService.decreaseQuantityByItemsSold(cartContent);
            session.removeAttribute("cartContent");
            return "redirect:/home";
        }

        return "redirect:/cart";
    }

}
