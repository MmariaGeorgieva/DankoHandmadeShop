package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.UserEditRequest;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ModelAndView showUserDetails(@PathVariable UUID id, Authentication userAuthentication) {
        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuthentication", userAuthentication);
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView updateUserProfile(@PathVariable UUID id,
                                          @Valid UserEditRequest userEditRequest,
                                          BindingResult bindingResult,
                                          Authentication userAuthentication,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            User user = userService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("user-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("userEditRequest", userEditRequest);
            modelAndView.addObject("userAuthentication", userAuthentication);
            return modelAndView;
        }
        userService.editUserDetails(id, userEditRequest);
        redirectAttributes.addFlashAttribute("successMessage", "User profile has been updated!");
        return new ModelAndView("redirect:/user/" + id);
    }

    @PutMapping("/{id}/subscription")
    public String updateUserRole(@PathVariable UUID id) {

        userService.switchSubscription(id);
        return "redirect:/user/" + id;
    }

    @GetMapping("/cart")
    public ModelAndView showCart(Authentication userAuthentication) {

        ModelAndView modelAndView = new ModelAndView();

        if (userAuthentication != null && userAuthentication.isAuthenticated()) {
            AuthenticationMetadata userData = (AuthenticationMetadata) userAuthentication.getPrincipal();
            UUID userId = userData.getUserId();
            User user = userService.getById(userId);
            modelAndView.addObject("user", user);
        }
        modelAndView.setViewName("cart");
        return modelAndView;
    }

}
