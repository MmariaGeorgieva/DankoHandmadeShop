package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.service.CloudinaryService;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.EditProductsPageRequest;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
        List<Product> activeProducts = productService.getAllActiveProducts();

        EditProductsPageRequest editProductsPageRequest = new EditProductsPageRequest();

        editProductsPageRequest.setActiveProducts(activeProducts.stream().map(product -> {
            EditProductsPageRequest.ProductEditRequest dto = new EditProductsPageRequest.ProductEditRequest();
            dto.setId(product.getId());
            dto.setStockQuantity(product.getStockQuantity());
            dto.setPrice(product.getPrice());
            dto.setMainImageUrl(product.getMainPhotoUrl());
            dto.setListingTitle(product.getListingTitle());
            return dto;
        }).toList());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("activeProducts", activeProducts);
        modelAndView.addObject("editProductsPageRequest", editProductsPageRequest);
        modelAndView.setViewName("products");

        return modelAndView;
    }

    @GetMapping("/products/inactive")
    public ModelAndView viewInactiveProducts() {
        List<Product> inactiveProducts = productService.getAllInactiveProducts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("inactive-products");
        modelAndView.addObject("inactiveProducts", inactiveProducts);
        return modelAndView;
    }

    @PutMapping("product/activate/{id}")
    public String activateProduct(@PathVariable UUID id) {
        productService.activateProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/products")
    public ModelAndView editProductsPage(@Valid @ModelAttribute("editProductsPageRequest") EditProductsPageRequest editProductsPageRequest,
                                         BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            productService.editProductsPage(editProductsPageRequest);
        }
        return viewProducts();
    }

    @GetMapping("/product/edit/{id}")
    public ModelAndView viewEditProductsPage(@PathVariable UUID id) {
        Product product = productService.getProductById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product-edit");
        modelAndView.addObject("editProductRequest", DtoMapper.mapProductToProductEditRequest(product));
        modelAndView.addObject("product", product);

        return modelAndView;
    }

    @PutMapping("/product/edit/{id}")
    public ModelAndView updateProduct(@PathVariable UUID id,
                                      @Valid EditProductRequest editProductRequest,
                                      BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            Product product = productService.getProductById(id);
            ModelAndView modelAndView = new ModelAndView("product-edit");
            modelAndView.addObject("product", product);
            modelAndView.addObject("editProductRequest", editProductRequest);
            return modelAndView;
        }

        productService.editProductDetails(id, editProductRequest);
        return new ModelAndView("redirect:/products");
    }

    @DeleteMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        System.out.println("Attempting to delete product with ID: " + id);
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/product/deleteMainPhoto/{productId}")
    public String deleteMainPhoto(@PathVariable UUID productId) {

        productService.deleteMainPhotoOfProductById(productId);
        return "redirect:/admin/product-edit";
    }
    @DeleteMapping("/product/deletePhoto/{productId}")
    public String deleteAdditionalPhoto(@PathVariable UUID productId) {

        productService.deleteAdditionalPhotoOfProductById(productId);
        return "redirect:/admin/product-edit";
    }


}
