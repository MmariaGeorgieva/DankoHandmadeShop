package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.order.service.OrderService;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.order.model.Order;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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

    @PostMapping("/my-orders/{userId}")
    public String completeOrder(@PathVariable UUID userId, HttpSession session, Authentication userAuthentication) {
        Map<Product, Integer> cartContent = (Map<Product, Integer>) session.getAttribute("cartContent");

        if (cartContent == null || cartContent.isEmpty()) {
            return "redirect:/cart";
        }
        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            User user = userService.getById(userId);

            orderService.createOrder(user, cartContent);

            productService.decreaseQuantityAndIncreaseItemsSold(cartContent);
            session.removeAttribute("cartContent");
            return "redirect:/orders/my-orders/" + userId;
        }
        return "redirect:/cart";
    }

    @GetMapping("/my-orders/{userId}")
    public ModelAndView GetMyOrders(@PathVariable UUID userId, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-orders");
        User user = userService.getById(userId);

        List<Order> myOrders = orderService.getAllOrdersByUserIdNewestFirst(userId);
        if (myOrders == null || myOrders.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "You have no orders yet!");
            return new ModelAndView("redirect:/home");
        }
        Map<Order, Map<Product, Integer>> ordersWithProducts = new LinkedHashMap<>();

        for (Order order : myOrders) {
            Map<Product, Integer> productsWithQuantity = new LinkedHashMap<>();
            for (Map.Entry<UUID, Integer> entry : order.getOrderedProducts().entrySet()) {
                Product product = productService.getProductById(entry.getKey());
                productsWithQuantity.put(product, entry.getValue());
            }
            ordersWithProducts.put(order, productsWithQuantity);
        }

        modelAndView.addObject("ordersWithProducts", ordersWithProducts);
        modelAndView.addObject("user", user);
        modelAndView.addObject("myOrders", myOrders);

        return modelAndView;
    }
}
