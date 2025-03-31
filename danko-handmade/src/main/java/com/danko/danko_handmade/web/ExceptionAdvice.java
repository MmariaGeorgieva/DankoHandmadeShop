package com.danko.danko_handmade.web;

import com.danko.danko_handmade.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(RedirectAttributes redirectAttributes,
                                                   HttpServletRequest request) {

        String username = request.getParameter("username");
        String message = "%s is already in use".formatted(username);

        redirectAttributes.addFlashAttribute("usernameAlreadyExistsMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExistsException(RedirectAttributes redirectAttributes) {

        String message = "The email you entered is already in use";

        redirectAttributes.addFlashAttribute("emailAlreadyExistsMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(RedirectAttributes redirectAttributes) {

        String message = "Product not found";
        redirectAttributes.addFlashAttribute("productNotFoundMessage", message);
        return "redirect:/products";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("MaxUploadSizeExceededMessage",
                "File size exceeds the maximum allowed limit!");
        return "redirect:/add-product";
    }

    @ExceptionHandler(ProductNotActiveException.class)
    public String handleUserNotActiveException(RedirectAttributes redirectAttributes) {
        return "redirect:/home";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,
            OrderNotFoundException.class,
            UserNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ModelAndView handleNotFoundException() {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());
        return modelAndView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to access this page.");
        return "redirect:/access-denied";
    }
}
