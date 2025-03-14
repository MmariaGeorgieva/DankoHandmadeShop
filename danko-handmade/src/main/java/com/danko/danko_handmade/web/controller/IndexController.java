package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.LoginRequest;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public IndexController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

//    @GetMapping("/")
//    public String getIndexPage() {
//
//        return "index";
//    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password.");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/home")
    public ModelAndView GetHomePage(Authentication user) {

        List<Product> activeProducts = productService.getAllActiveProducts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("activeProducts", activeProducts);
        if (user != null && user.isAuthenticated()) {
            modelAndView.addObject("user", user);
            user.getAuthorities().forEach(grantedAuthority ->
                    System.out.println("Authority: " + grantedAuthority.getAuthority()));

            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                modelAndView.setViewName("admin");
            } else {
                modelAndView.setViewName("home");
            }
        }
        return modelAndView;
    }

    @PostMapping("/home")
    public ModelAndView viewHomePage() {
        return null;
    }

    @GetMapping("/about")
    public ModelAndView getAboutPage(Authentication user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("about");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/shop-policies")
    public ModelAndView getPoliciesPage(Authentication user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shop-policies");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage(Authentication user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("contact");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/faq")
    public ModelAndView getFaqsPage(Authentication user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("faq");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

}
