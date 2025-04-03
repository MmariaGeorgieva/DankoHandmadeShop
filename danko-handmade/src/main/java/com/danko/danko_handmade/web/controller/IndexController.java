package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.ContactShopRequest;
import com.danko.danko_handmade.web.dto.LoginRequest;
import com.danko.danko_handmade.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;
    private  final EmailService emailService;

    @Autowired
    public IndexController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }

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

    @GetMapping("/about")
    public ModelAndView getAboutPage(@AuthenticationPrincipal UserDetails user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("about");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/shop-policies")
    public ModelAndView getPoliciesPage(@AuthenticationPrincipal UserDetails user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shop-policies");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage(Authentication userAuthentication,
                                       @RequestParam(value = "success", required = false) String success) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("contact");

        if ("true".equals(success)) {
            modelAndView.addObject("successMessage", "Your message has been sent successfully. We will get back to you soon.");
        }

        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID userId = userData.getUserId();
            User user = userService.getById(userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuthentication", userAuthentication);
        }
        modelAndView.addObject("contactShopRequest", new ContactShopRequest());
        return modelAndView;
    }

    @PostMapping("/contact")
    public String sendEmailThroughContactForm(@Valid ContactShopRequest contactShopRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "contact";
        }

        emailService.sendEmailThroughContactForm(contactShopRequest);
        return "redirect:/contact?success=true";
    }

    @GetMapping("/faq")
    public ModelAndView getFaqsPage(@AuthenticationPrincipal UserDetails user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("faq");
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

}
