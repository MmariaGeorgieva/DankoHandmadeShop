package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.exception.ProductNotActiveException;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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
    public ModelAndView GetHomePage(Authentication userAuthentication) {

        List<Product> activeProducts = productService.getAllActiveProducts();
        UUID userId = null;
        ModelAndView modelAndView = new ModelAndView();

        if (userAuthentication != null) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            userId = userData.getUserId();
            modelAndView.addObject("userId", userId);
        }
        modelAndView.addObject("activeProducts", activeProducts);
        modelAndView.addObject("userAuthentication", userAuthentication);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/active-product/{productId}")
    public ModelAndView showActiveProduct(@PathVariable UUID productId, Authentication user) {

        Product activeProduct = productService.getActiveProductById(productId);

        LocalDate today = LocalDate.now();
        LocalDate arrivalStartDate = today.plusDays(10);
        LocalDate arrivalEndDate = today.plusDays(16);

        String arrivalStart = DateUtil.getFormattedDateWithSuffix(arrivalStartDate);
        String arrivalEnd = DateUtil.getFormattedDateWithSuffix(arrivalEndDate);

        String arrivalPeriod = arrivalStart + " - " + arrivalEnd;

        List<Product> relatedProducts = productService.getSixRandomProductsFromTheSameSection(activeProduct);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("active-product");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        modelAndView.addObject("activeProduct", activeProduct);
        modelAndView.addObject("arrivalPeriod", arrivalPeriod);
        modelAndView.addObject("relatedProducts", relatedProducts);
        return modelAndView;
    }

    @GetMapping("/user/{id}")
    public ModelAndView showUserDetails(@PathVariable UUID id, Authentication userAuth) {
        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }
}
