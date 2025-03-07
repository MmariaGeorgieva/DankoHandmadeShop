package com.danko.danko_handmade.product.service;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static int start = 1000;

    private final ProductRepository productRepository;
    private final CloudflareR2Service cloudflareR2Service;

    @Autowired
    public ProductService(ProductRepository productRepository, CloudflareR2Service cloudflareR2Service) {
        this.productRepository = productRepository;
        this.cloudflareR2Service = cloudflareR2Service;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void createProduct(AddProductRequest addProductRequest,
                              MultipartFile mainPhoto, List<MultipartFile> additionalPhotos)
            throws IOException {

        String productCode = generateProductCode(addProductRequest);

        String mainPhotoFileName = System.currentTimeMillis() + "_" + mainPhoto.getOriginalFilename();
        String mainPhotoUrl = cloudflareR2Service.uploadFile(mainPhoto.getOriginalFilename(),
                "products/main/" + mainPhotoFileName);

        List<String> additionalPhotosUrls = new ArrayList<>();

        for (MultipartFile additionalPhoto : additionalPhotos) {
            String additionalPhotoFileName = System.currentTimeMillis() + "_" + additionalPhoto.getOriginalFilename();
            String url = cloudflareR2Service.uploadFile(additionalPhoto.getOriginalFilename(),
                    "products/additional/" + additionalPhotoFileName);
            additionalPhotosUrls.add(url);
        }

        Product product = Product.builder()
                .listingTitle(addProductRequest.getListingTitle())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .productCode(productCode)
                .mainPhotoUrl(mainPhotoUrl)
                .additionalPhotos(additionalPhotosUrls)
                .addedOn(LocalDateTime.now())
                .weight(addProductRequest.getWeight())
                .stockQuantity(addProductRequest.getStockQuantity())
                .build();

        product.getProductSection().add(ProductSection.ALL);
        product.getProductSection().add(addProductRequest.getProductSection());

        productRepository.save(product);
    }

    private String generateProductCode(AddProductRequest addProductRequest) {

        String prefix = switch (addProductRequest.getProductSection().toString()) {
            case "CUPS_AND_MUGS" -> "C&M";
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
}
