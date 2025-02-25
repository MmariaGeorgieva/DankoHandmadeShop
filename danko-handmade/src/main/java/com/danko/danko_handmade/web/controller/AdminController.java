package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.config.SecurityConfiguration;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    //hasAnyRole - checking for one of the roles that follow
    //hasRole - checking for exactly the one given role
    //hasAuthority - works exactly the same way, but checks for a permission
    //hasAnyAuthority - works exactly the same way, but checks for one of the following permissions

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAdminPanel(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        ModelAndView mav = new ModelAndView("admin");

        return mav;
    }

    @GetMapping("/add-product")
    public ModelAndView getAddProductForm() {
        ModelAndView modelAndView = new ModelAndView("add-product");
        modelAndView.addObject("addProductRequest", new AddProductRequest());
        return modelAndView;
    }

    @PostMapping("/add-product")
    public String addProduct(@Valid AddProductRequest addProductRequest,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (bindingResult.hasErrors()) {
            return "add-product";
        }
        productService.createProduct(addProductRequest);
        return "redirect:/admin/products";
    }


//    @GetMapping("/products")
//    public ModelAndView viewProducts() {
//        List<Product> products = productService.getAllProducts();
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("products", products);
//        modelAndView.setViewName("admin/products");
//
//        return modelAndView;
//    }
}
