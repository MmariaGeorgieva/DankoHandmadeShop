package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.CloudinaryService;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductsPageRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    public AdminController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
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
                             @RequestParam("mainPhoto") MultipartFile mainPhoto,
                             @RequestParam("additionalPhotos") List<MultipartFile> additionalPhotos,
                             @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) throws IOException {
        if (bindingResult.hasErrors()) {
            return "add-product";
        }

        productService.createProduct(addProductRequest, mainPhoto, additionalPhotos);
        return "redirect:/admin/products";
    }


    @GetMapping("/products")
    public ModelAndView viewProducts() {
        List<Product> products = productService.getAllProducts();

        EditProductsPageRequest editProductsPageRequest = new EditProductsPageRequest();
        editProductsPageRequest.setProducts(products.stream().map(product -> {
            EditProductsPageRequest.ProductEditRequest dto = new EditProductsPageRequest.ProductEditRequest();
            dto.setId(product.getId());
            dto.setStockQuantity(product.getStockQuantity());
            dto.setPrice(product.getPrice());
            return dto;
        }).toList());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        modelAndView.addObject("editProductsPageRequest", editProductsPageRequest);
        modelAndView.setViewName("products");

        return modelAndView;
    }

    @PostMapping("/products")
    public ModelAndView editProductsPage(@Valid @ModelAttribute("editProductsPageRequest") EditProductsPageRequest editProductsPageRequest,
                                         BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            productService.editProductsPage(editProductsPageRequest);
        }
        return viewProducts();
    }
}
