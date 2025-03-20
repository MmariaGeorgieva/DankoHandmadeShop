package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.security.AuthenticationMetadata;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final UserService userService;


    public AdminController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAdminPanel(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        return new ModelAndView("admin");
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
    public ModelAndView viewProducts(@RequestParam(value = "section", required = false) String section) {

        ModelAndView modelAndView = new ModelAndView();
        List<Product> activeProducts = productService.getAllActiveProducts();
        modelAndView.addObject("selectedSection", section);

        if (section != null && !section.isEmpty()) {
            if (section.equals("ALL")) {
                activeProducts = productService.getAllActiveProducts();
            } else {
                activeProducts = productService.getAllActiveProductsBySection(ProductSection.valueOf(section));
            }
        }

        modelAndView.addObject("activeProducts", activeProducts);
        modelAndView.addObject("productSections", ProductSection.values());
        modelAndView.setViewName("products");

        return modelAndView;
    }


    @GetMapping("/users")
    public ModelAndView viewUsers() {
        List<User> allUsers = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", allUsers);
        modelAndView.setViewName("users");

        return modelAndView;
    }

    @PutMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable UUID id) {

        userService.switchRole(id);
        return "redirect:/admin/users";
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
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Product product = productService.getProductById(id);
            ModelAndView modelAndView = new ModelAndView("product-edit");
            modelAndView.addObject("product", product);
            modelAndView.addObject("editProductRequest", editProductRequest);
            return modelAndView;
        }
        productService.editProductDetails(id, editProductRequest);
        return new ModelAndView("redirect:/admin/product/edit/" + id);
    }

    @PutMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deactivateProductById(id);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/product/deleteMainPhoto/{productId}")
    public String deleteMainPhoto(@PathVariable UUID productId) throws URISyntaxException {

        productService.deleteMainPhotoOfProductWithId(productId);
        return "redirect:/admin/product/edit/" + productId;
    }

    @PostMapping("product/uploadMainPhoto/{productId}")
    public String uploadMainPhoto(@PathVariable UUID productId, @RequestParam("mainPhoto") MultipartFile file) throws IOException {
        productService.uploadNewMainPhoto(productId, file);
        return "redirect:/admin/product/edit/" + productId;
    }

    @DeleteMapping("/product/deleteAdditionalPhoto/{productId}/{photoIndex}")
    public String deleteAdditionalPhoto(@PathVariable UUID productId, @PathVariable int photoIndex) throws URISyntaxException {
        productService.deleteAdditionalPhoto(productId, photoIndex);
        return "redirect:/admin/product/edit/" + productId;
    }


    @PostMapping("product/uploadAdditionalPhoto/{productId}")
    public String uploadAdditionalPhoto(@PathVariable UUID productId,
                                    @RequestParam("additionalPhoto") MultipartFile file) throws IOException {
        productService.uploadAdditionalPhoto(productId, file);
        return "redirect:/admin/product/edit/" + productId;
    }



}
