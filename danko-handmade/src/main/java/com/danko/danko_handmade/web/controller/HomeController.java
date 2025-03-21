package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.AddToCartRequest;
import com.danko.danko_handmade.web.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public HomeController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView GetHomePage(@RequestParam(value = "section", required = false) String section,
                                    AddToCartRequest addToCartRequest,
                                    Authentication userAuthentication) {

        ModelAndView modelAndView = new ModelAndView();

        if (userAuthentication != null) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            User user = userService.getById(userData.getUserId());
            modelAndView.addObject("user", user);
            modelAndView.addObject("authorities", userData.getAuthorities());
        }

        List<Product> activeProducts = productService.getAllActiveProducts();
        modelAndView.addObject("selectedSection", section);

        if (section != null && !section.isEmpty()) {
            if (section.equals("ALL")) {
                activeProducts = productService.getAllActiveProducts();
            } else {
                activeProducts = productService.getAllActiveProductsBySection(ProductSection.valueOf(section));
            }
        }
        modelAndView.addObject("activeProducts", activeProducts);
        modelAndView.addObject("userAuthentication", userAuthentication);
        modelAndView.addObject("productSections", ProductSection.values());
        modelAndView.addObject("addToCartRequest", new AddToCartRequest());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/active-product/{productId}")
    public ModelAndView showActiveProduct(@PathVariable UUID productId,
                                          Authentication userAuthentication) {

        Product activeProduct = productService.getActiveProductById(productId);

        LocalDate today = LocalDate.now();
        LocalDate arrivalStartDate = today.plusDays(10);
        LocalDate arrivalEndDate = today.plusDays(16);

        String arrivalStart = DateUtil.getFormattedDateWithSuffix(arrivalStartDate);
        String arrivalEnd = DateUtil.getFormattedDateWithSuffix(arrivalEndDate);
        String arrivalPeriod = arrivalStart + " - " + arrivalEnd;

        List<Product> relatedProducts = productService.findSixRandomProducts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("active-product");
        if (userAuthentication != null) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            User user = userService.getById(userData.getUserId());
            modelAndView.addObject("user", user);
            modelAndView.addObject("authorities", userData.getAuthorities());
        }
        modelAndView.addObject("activeProduct", activeProduct);
        modelAndView.addObject("arrivalPeriod", arrivalPeriod);
        modelAndView.addObject("relatedProducts", relatedProducts);
        modelAndView.addObject("userAuthentication", userAuthentication);
        modelAndView.addObject("addToCartRequest", new AddToCartRequest());
        return modelAndView;
    }
}
