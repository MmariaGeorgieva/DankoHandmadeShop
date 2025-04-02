package com.danko.danko_handmade.product.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.danko.danko_handmade.exception.ProductNotActiveException;
import com.danko.danko_handmade.exception.ProductNotFoundException;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final Cloudinary cloudinary;

    @Autowired
    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService, Cloudinary cloudinary) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
        this.cloudinary = cloudinary;
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
                .productSection(addProductRequest.getProductSection())
                .addedOn(LocalDateTime.now())
                .stockQuantity(addProductRequest.getStockQuantity())
                .active(true)
                .build();

        productRepository.save(product);
    }

    public String generateProductCode(AddProductRequest addProductRequest) {

        String prefix = switch (addProductRequest.getProductSection().toString()) {
            case "CUPS_AND_MUGS" -> "C&M";
            case "HALLOWEEN" -> "HAllO";
            case "TEAPOTS" -> "TEA";
            case "SUGAR_BOWLS" -> "SCC";
            case "PITCHERS" -> "PJB";
            case "SETS" -> "SET";
            case "TRAYS" -> "TPW";
            case "HOME_DECOR" -> "HOME";
            case "TILES" -> "TILE";
            default -> "ALL";
        };

        Product lastProduct = productRepository.findTopByOrderByAddedOnDesc();

        int lastNumber = 1011;
        if (lastProduct != null) {
            String lastProductCode = lastProduct.getProductCode();
            String[] parts = lastProductCode.split("-");
            lastNumber = Integer.parseInt(parts[1]) + 1;
        }
        return prefix + "-" + lastNumber;
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public void editProductDetails(UUID id, EditProductRequest editProductRequest) {
        Product product = getProductById(id);

        product.setListingTitle(editProductRequest.getListingTitle());
        product.setDescription(editProductRequest.getDescription());
        product.setPrice(editProductRequest.getPrice());
        product.setProductSection(editProductRequest.getProductSection());
        product.setStockQuantity(editProductRequest.getStockQuantity());
        product.setActive(editProductRequest.isActive());

        productRepository.save(product);
    }

    public void deactivateProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setActive(false);
        productRepository.save(product);
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findAllByActiveOrderByAddedOnDesc(true);
    }

    public List<Product> getAllInactiveProducts() {
        return productRepository.findAllByActiveOrderByAddedOnDesc(false);
    }

    public void activateProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setActive(true);
        productRepository.save(product);
    }

    @Transactional
    public void deleteMainPhotoOfProductWithId(UUID productId) throws URISyntaxException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (product.getMainPhotoUrl() != null) {
            String mainPhotoUrl = product.getMainPhotoUrl();
            String publicId = extractPublicIdFromUrl(mainPhotoUrl);

            try {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                product.setMainPhotoUrl(null);
                productRepository.save(product);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String extractPublicIdFromUrl(String mainPhotoUrl) throws URISyntaxException {
        try {
            URI uri = new URI(mainPhotoUrl);
            String path = uri.getPath();
            String[] segments = path.split("/");

            String filenameWithExtension = segments[segments.length - 1];
            return filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf('.'));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid Cloudinary URL: " + mainPhotoUrl);
        }
    }

    public void uploadNewMainPhoto(UUID productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setMainPhotoUrl(cloudinaryService.uploadPhoto(file, "products/main/"));
        productRepository.save(product);
    }

    @Transactional
    public void deleteAdditionalPhoto(UUID productId, int photoIndex) throws URISyntaxException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        List<String> additionalPhotos = product.getAdditionalPhotosUrls();

        if (additionalPhotos == null || photoIndex < 0 || photoIndex >= additionalPhotos.size()) {
            throw new IllegalArgumentException("Invalid photo index");
        }

        String additionalPhotoUrl = additionalPhotos.get(photoIndex);
        if (additionalPhotoUrl != null && !additionalPhotoUrl.isEmpty()) {
            String publicId = extractPublicIdFromUrl(additionalPhotoUrl);

            try {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete photo from Cloudinary", e);
            }
        }
        additionalPhotos.remove(photoIndex);
        productRepository.save(product);
    }

    public void uploadAdditionalPhoto(UUID productId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be empty");
        }

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        String newAdditionalPhotoUrl = cloudinaryService.uploadPhoto(file, "products/additional/");
        product.getAdditionalPhotosUrls().add(newAdditionalPhotoUrl);
        productRepository.save(product);
    }

    public List<Product> findSixRandomProducts() {
        Pageable pageable = PageRequest.of(0, 6);
        return productRepository.findSixRandomProducts(pageable);
    }

    public Product getActiveProductById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        Product activeProduct = null;
        if (product.isActive()) {
            activeProduct = product;
        } else {
            throw new ProductNotActiveException("Product not active");
        }
        return activeProduct;
    }

    public List<Product> getAllActiveProductsBySection(ProductSection productSection) {
        return productRepository.findAllByActiveAndProductSection(true, productSection);
    }

    public List<String> getMainPhotosNewProducts() {
         List<Product> newProducts =
                 productRepository.findAllByOrderByAddedOnDesc();
         return newProducts.stream()
                 .map(Product::getMainPhotoUrl)
                 .toList();
    }

    public List<String> getBestSellers() {
        List<Product> bestsellers = productRepository.findAllByOrderByItemsSoldDesc();
        return bestsellers.stream()
                .map(Product::getMainPhotoUrl)
                .toList();
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void decreaseQuantityAndIncreaseItemsSold(Map<Product, Integer> cartContent) {
        for(Map.Entry<Product, Integer> entry : cartContent.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            if(product.getStockQuantity() >= quantity) {
                product.setStockQuantity(product.getStockQuantity() - quantity);
                product.setItemsSold(product.getItemsSold() + quantity);
                if (product.getStockQuantity() == 0) {
                    product.setActive(false);
                }
                productRepository.save(product);
            }
        }
    }
}
