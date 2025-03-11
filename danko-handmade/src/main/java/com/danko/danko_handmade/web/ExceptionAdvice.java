package com.danko.danko_handmade.web;

import com.danko.danko_handmade.exception.UsernameAlreadyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("usernameAlreadyExistsMessage", "Username already exists");
        return "redirect:/register";
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
