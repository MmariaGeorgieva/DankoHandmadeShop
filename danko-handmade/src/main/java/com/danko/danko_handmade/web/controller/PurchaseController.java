package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.AddToCartRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PurchaseController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public PurchaseController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public ModelAndView getCartPage(Authentication userAuthentication, HttpSession session) {

        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            Map<Product, Integer> cartContent = (Map<Product, Integer>) session.getAttribute("cartContent");
            if (cartContent == null) {
                cartContent = new HashMap<>();
            }
            BigDecimal totalPrice = cartContent.entrySet().stream()
                    .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cart");
            modelAndView.addObject("cartContent", cartContent);
            modelAndView.addObject("totalPrice", totalPrice);

            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID userId = userData.getUserId();
            User user = userService.getById(userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuthentication", userAuthentication);

            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @PostMapping("/cart/add")
    public String addToCart(AddToCartRequest addToCartRequest,
                            Authentication userAuthentication,
                            HttpSession session) {
        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            UUID productId = addToCartRequest.getProductId();
            int quantity = addToCartRequest.getQuantity();
            Product product = productService.getProductById(productId);

            Map<Product, Integer> cartContent = (Map<Product, Integer>) session.getAttribute("cartContent");
            if (cartContent == null) {
                cartContent = new HashMap<>();
            }

            cartContent.put(product, quantity);
            session.setAttribute("cartContent", cartContent);
        }
        return "redirect:/cart";
    }

    @DeleteMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable UUID id, HttpSession session) {
        Map<Product, Integer> cartContent = (Map<Product, Integer>) session.getAttribute("cartContent");

        if (cartContent != null) {
            cartContent.entrySet().removeIf(entry -> entry.getKey().getId().equals(id));
            session.setAttribute("cartContent", cartContent);
        }
        return "redirect:/cart";
    }
}
