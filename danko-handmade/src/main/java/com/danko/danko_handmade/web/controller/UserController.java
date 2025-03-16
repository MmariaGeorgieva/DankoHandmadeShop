package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getHomePage(@PathVariable UUID id, @RequestParam(defaultValue = "1") int page) {
//        User user = userService.getById(id);
//        int pageSize = 50;
//        Page<Product> productPage = productService.getProducts(page, pageSize);
//
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("home");
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("products", productPage.getContent());
//        modelAndView.addObject("productPage", productPage);
//        modelAndView.addObject("totalSales", 1200);
//        modelAndView.addObject("totalReviews", 250);
//        modelAndView.addObject("averageRating", 4.5);
//        modelAndView.addObject("starsHtml", "★★★★☆");
//        modelAndView.addObject("announcement", "Spring Collection Now Available!");
//        modelAndView.addObject("currentPage", productPage.getNumber() + 1);
//        modelAndView.addObject("totalPages", productPage.getTotalPages());

        return modelAndView;
    }


}
