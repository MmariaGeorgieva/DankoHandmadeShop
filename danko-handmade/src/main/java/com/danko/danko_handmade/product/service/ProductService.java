package com.danko.danko_handmade.product.service;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.EditProductsPageRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {
    private static int start = 1000;

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void createProduct(AddProductRequest addProductRequest,
                              MultipartFile mainPhoto,
                              List<MultipartFile> additionalPhotos) throws IOException {

        String productCode = generateProductCode(addProductRequest);

        String mainPhotoUrl = cloudinaryService.uploadPhoto(mainPhoto, "products/main/");

        List<String> additionalPhotosUrls = new ArrayList<>();
        for (MultipartFile additionalPhoto : additionalPhotos) {
            String additionalPhotoUrl = cloudinaryService.uploadPhoto(additionalPhoto, "products/additional/");
            additionalPhotosUrls.add(additionalPhotoUrl);
        }

        Product product = Product.builder()
                .listingTitle(addProductRequest.getListingTitle())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .productCode(productCode)
                .mainPhotoUrl(mainPhotoUrl)
                .additionalPhotos(additionalPhotosUrls)
                .productSection(new ArrayList<>())
                .addedOn(LocalDateTime.now())
                .weight(addProductRequest.getWeight())
                .stockQuantity(addProductRequest.getStockQuantity())
                .build();

        product.setProductSection(List.of(ProductSection.ALL, addProductRequest.getProductSection()));
        productRepository.save(product);
    }

    private String generateProductCode(AddProductRequest addProductRequest) {

        String prefix = switch (addProductRequest.getProductSection().toString()) {
            case "CUPS_AND_MUGS" -> "C&M";
            case "All" -> "ALL";
            case "HAPPY_HALLOWEEN" -> "HAllO";
            case "TEAPOTS" -> "TEA";
            case "SUGAR_CREAMERS_CANISTERS" -> "SCC";
            case "PITCHERS_JUGS_BOTTLES" -> "PJB";
            case "SETS" -> "SET";
            case "TRAYS_PLATES_WALL" -> "TPW";
            case "HOME_DECOR" -> "HOME";
            case "TILES" -> "TILE";
            case "CHRISTMAS_GIFTS" -> "XMAS";
            default -> "";
        };

        int suffix = start++;
        return prefix + "-" + suffix;
    }

    public void editProductsPage(EditProductsPageRequest editProductsPageRequest) {
        for (EditProductsPageRequest.ProductEditRequest dto : editProductsPageRequest.getProducts()) {
            Product product = productRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getPrice().compareTo(dto.getPrice()) != 0 || product.getStockQuantity() != dto.getStockQuantity()) {
                product.setPrice(dto.getPrice());
                product.setStockQuantity(dto.getStockQuantity());
                productRepository.save(product);
            }
        }
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void editProductDetails(UUID id, EditProductRequest editProductRequest) {

        Product product = getProductById(id);

        product.setListingTitle(editProductRequest.getListingTitle());
        product.setDescription(editProductRequest.getDescription());
        product.setPrice(editProductRequest.getPrice());
        product.setProductSection(editProductRequest.getProductSection());
        product.setWeight(editProductRequest.getWeight());
        product.setStockQuantity(editProductRequest.getStockQuantity());
        product.setMainPhotoUrl(editProductRequest.getMainPhotoUrl());
        product.setAdditionalPhotos(editProductRequest.getAdditionalPhotos());

        productRepository.save(product);
    }

    public Page<Product> getProducts(int page, int pageSize) {
        return null;
    }
}
