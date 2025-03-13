package com.danko.danko_handmade.product.service;

import com.danko.danko_handmade.exception.ProductNotFoundException;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.EditProductsPageRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                .additionalPhotosUrls(additionalPhotosUrls)
                .productSection(new ArrayList<>())
                .addedOn(LocalDateTime.now())
                .weight(addProductRequest.getWeight())
                .stockQuantity(addProductRequest.getStockQuantity())
                .active(true)
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

    @Transactional
    public void editProductsPage(EditProductsPageRequest editProductsPageRequest) {
        for (EditProductsPageRequest.ProductEditRequest dto : editProductsPageRequest.getActiveProducts()) {
            Product product = productRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getPrice().compareTo(dto.getPrice()) != 0 || product.getStockQuantity() != dto.getStockQuantity()) {
                product.setPrice(dto.getPrice());
                product.setStockQuantity(dto.getStockQuantity());
                productRepository.save(product);
            }
        }
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    public void editProductDetails(UUID id, EditProductRequest editProductRequest) throws IOException {

        Product product = getProductById(id);

        product.setListingTitle(editProductRequest.getListingTitle());
        product.setDescription(editProductRequest.getDescription());
        product.setPrice(editProductRequest.getPrice());
        product.setProductSection(List.of(ProductSection.ALL, editProductRequest.getProductSection()));
        product.setWeight(editProductRequest.getWeight());
        product.setStockQuantity(editProductRequest.getStockQuantity());

        if (editProductRequest.getNewMainPhoto() != null && !editProductRequest.getNewMainPhoto().isEmpty()) {
            try {
                String newMainPhotoUrl = cloudinaryService.uploadPhoto(editProductRequest.getNewMainPhoto(), "main");
                product.setMainPhotoUrl(newMainPhotoUrl);
            } catch (IOException e) {
                System.err.println("Error uploading main photo: " + e.getMessage());
            }
        } else if (editProductRequest.getExistingMainPhotoUrl() != null && !editProductRequest.getExistingMainPhotoUrl().isEmpty()) {
            product.setMainPhotoUrl(editProductRequest.getExistingMainPhotoUrl());
        }

        List<String> updatedPhotos = new ArrayList<>(editProductRequest.getExistingAdditionalPhotosUrls());
        if (editProductRequest.getNewAdditionalPhotos() != null && !editProductRequest.getNewAdditionalPhotos().isEmpty()) {
            for (MultipartFile file : editProductRequest.getNewAdditionalPhotos()) {
                try {
                    String photoUrl = cloudinaryService.uploadPhoto(file, "additional");
                    updatedPhotos.add(photoUrl);
                } catch (IOException e) {
                    System.err.println("Error uploading additional photo: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No additional photos uploaded.");
        }

        product.setAdditionalPhotosUrls(updatedPhotos);
        productRepository.save(product);
    }

    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findAllByActive(true);
    }

    public List<Product> getAllInactiveProducts() {
        return productRepository.findAllByActive(false);
    }

    public void activateProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setActive(true);
        productRepository.save(product);
    }

    public void deleteMainPhotoOfProductById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setMainPhotoUrl(null);

    }

    public void deleteAdditionalPhotoOfProductById(UUID productId) {
    }
}
