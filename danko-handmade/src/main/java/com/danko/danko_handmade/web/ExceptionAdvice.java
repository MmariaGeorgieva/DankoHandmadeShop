package com.danko.danko_handmade.web;

import com.danko.danko_handmade.exception.ProductNotActiveException;
import com.danko.danko_handmade.exception.ProductNotFoundException;
import com.danko.danko_handmade.exception.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

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

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleUserAlreadyExistsException(RedirectAttributes redirectAttributes) {
        return "redirect:/products";
    }

    @ExceptionHandler(ProductNotActiveException.class)
    public String handleUserNotActiveException(RedirectAttributes redirectAttributes) {
        return "redirect:/home";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundException(RedirectAttributes redirectAttributes) {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());
        return modelAndView;
    }


//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView handleRuntimeException() {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("internal-server-error");
//        return mav;
//    }
}
