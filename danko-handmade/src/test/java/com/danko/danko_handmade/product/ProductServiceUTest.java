package com.danko.danko_handmade.product;

import com.cloudinary.Cloudinary;
import com.danko.danko_handmade.exception.ProductNotActiveException;
import com.danko.danko_handmade.exception.ProductNotFoundException;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.product.service.CloudinaryService;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.web.dto.AddProductRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private ProductService productService;

    @Test
    void givenProductExists_whenGetProductById_thenReturnProduct() {

        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .listingTitle("Test Product")
                .price(BigDecimal.valueOf(100))
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(productId);
        assertEquals(product, foundProduct);
    }

    @Test
    void givenProductDoesNotExist_whenGetProductById_thenExceptionIsThrown() {

        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    void givenValidProductId_whenEditProductDetails_thenProductIsUpdated() {

        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .listingTitle("Old Title")
                .description("Old Description")
                .price(BigDecimal.valueOf(50))
                .productSection(ProductSection.HALLOWEEN)
                .stockQuantity(10)
                .active(true)
                .build();

        EditProductRequest editProductRequest = new EditProductRequest();
        editProductRequest.setListingTitle("New Title");
        editProductRequest.setDescription("New Description");
        editProductRequest.setPrice(BigDecimal.valueOf(100));
        editProductRequest.setProductSection(ProductSection.CUPS_AND_MUGS);
        editProductRequest.setStockQuantity(20);
        editProductRequest.setActive(false);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.editProductDetails(productId, editProductRequest);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();

        assertEquals(existingProduct.getListingTitle(), updatedProduct.getListingTitle());
        assertEquals(existingProduct.getDescription(), updatedProduct.getDescription());
        assertEquals(existingProduct.getPrice(), updatedProduct.getPrice());
        assertEquals(existingProduct.getProductSection(), updatedProduct.getProductSection());
        assertEquals(existingProduct.getStockQuantity(), updatedProduct.getStockQuantity());
        assertFalse(updatedProduct.isActive());
        verify(productRepository, times(1)).save(productCaptor.capture());
    }

    @Test
    void givenInvalidProductId_whenEditProductDetails_thenThrowProductNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        EditProductRequest editProductRequest = new EditProductRequest();
        assertThrows(ProductNotFoundException.class, () -> productService.editProductDetails(productId, editProductRequest));
    }

    @Test
    void givenValidProductId_whenDeactivateProductById_thenProductIsDeactivated() {

        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .active(true)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deactivateProductById(productId);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();

        assertFalse(updatedProduct.isActive());
        verify(productRepository, times(1)).save(productCaptor.capture());
    }

    @Test
    void givenInvalidProductId_whenDeactivateProductById_thenThrowProductNotFoundException() {

        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deactivateProductById(productId));
    }

    @Test
    void givenActiveProductsExist_whenGetAllActiveProducts_thenReturnSortedList() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .active(true)
                .addedOn(LocalDateTime.now().minusDays(1))
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 2")
                .active(true)
                .addedOn(LocalDateTime.now())
                .build();

        List<Product> activeProducts = List.of(product2, product1);

        when(productRepository.findAllByActiveOrderByAddedOnDesc(true)).thenReturn(activeProducts);

        List<Product> result = productService.getAllActiveProducts();

        assertEquals(2, result.size());
        assertEquals(product2, result.get(0));
        assertEquals(product1, result.get(1));
    }

    @Test
    void givenNoActiveProductsExist_whenGetAllActiveProducts_thenReturnEmptyList() {
        when(productRepository.findAllByActiveOrderByAddedOnDesc(true)).thenReturn(List.of());

        List<Product> result = productService.getAllActiveProducts();
        assertTrue(result.isEmpty());
    }

    @Test
    void givenInactiveProductsExist_whenGetAllInactiveProducts_thenReturnSortedList() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .active(false)
                .addedOn(LocalDateTime.now().minusDays(1))
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 2")
                .active(false)
                .addedOn(LocalDateTime.now())
                .build();

        List<Product> inactiveProducts = List.of(product2, product1);

        when(productRepository.findAllByActiveOrderByAddedOnDesc(false)).thenReturn(inactiveProducts);

        List<Product> result = productService.getAllInactiveProducts();

        assertEquals(2, result.size());
        assertEquals(product2, result.get(0));
        assertEquals(product1, result.get(1));
    }

    @Test
    void givenNoInactiveProductsExist_whenGetAllInactiveProducts_thenReturnEmptyList() {
        when(productRepository.findAllByActiveOrderByAddedOnDesc(false)).thenReturn(List.of());

        List<Product> result = productService.getAllInactiveProducts();
        assertTrue(result.isEmpty());
    }

    @Test
    void givenInvalidProductId_whenActivateProductById_thenProductIsActivated() {

        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .active(false)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.activateProduct(productId);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();

        assertTrue(updatedProduct.isActive());
        verify(productRepository, times(1)).save(productCaptor.capture());
    }

    @Test
    void givenInvalidProductId_whenActivateProductById_thenThrowProductNotFoundException() {

        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.activateProduct(productId));
    }

    @Test
    void whenFindSixRandomProducts_thenReturnsProducts() {

        Product product1 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 1").build();
        Product product2 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 2").build();
        Product product3 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 3").build();
        Product product4 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 4").build();
        Product product5 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 5").build();
        Product product6 = Product.builder().id(UUID.randomUUID()).listingTitle("Product 6").build();

        List<Product> mockProducts = List.of(product1, product2, product3, product4, product5, product6);

        when(productRepository.findSixRandomProducts(any(Pageable.class))).thenReturn(mockProducts);

        List<Product> result = productService.findSixRandomProducts();

        assertEquals(6, result.size());
        assertTrue(result.containsAll(mockProducts));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findSixRandomProducts(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(6, capturedPageable.getPageSize());
    }

    @Test
    void whenNoProductsAvailable_thenReturnsEmptyList() {
        when(productRepository.findSixRandomProducts(any(Pageable.class))).thenReturn(List.of());

        List<Product> result = productService.findSixRandomProducts();

        assertTrue(result.isEmpty());
    }

    @Test
    void givenProductIsActive_whenGetActiveProductById_thenReturnProduct() {

        UUID productId = UUID.randomUUID();
        Product activeProduct = Product.builder()
                .id(productId)
                .listingTitle("Active Product")
                .active(true)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(activeProduct));

        Product result = productService.getActiveProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    @Test
    void givenProductIsNotActive_whenGetActiveProductById_thenThrowProductNotActiveException() {

        UUID productId = UUID.randomUUID();
        Product inactiveProduct = Product.builder()
                .id(productId)
                .listingTitle("Inactive Product")
                .active(false)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(inactiveProduct));
        assertThrows(ProductNotActiveException.class, () -> productService.getActiveProductById(productId));
    }

    @Test
    void givenProductDoesNotExist_whenGetActiveProductById_thenThrowProductNotFoundException() {

        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getActiveProductById(productId));
    }

    @Test
    void givenActiveProductsExistInSection_whenGetAllActiveProductsBySection_thenReturnProductList() {

        ProductSection section = ProductSection.HALLOWEEN;

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Ceramic Bowl")
                .active(true)
                .productSection(section)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Ceramic Plate")
                .active(true)
                .productSection(section)
                .build();

        List<Product> expectedProducts = List.of(product1, product2);

        when(productRepository.findAllByActiveAndProductSection(true, section))
                .thenReturn(expectedProducts);
        List<Product> result = productService.getAllActiveProductsBySection(section);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(product1) && result.contains(product2));
    }

    @Test
    void givenNoActiveProductsInSection_whenGetAllActiveProductsBySection_thenReturnEmptyList() {

        ProductSection section = ProductSection.HALLOWEEN;

        when(productRepository.findAllByActiveAndProductSection(true, section))
                .thenReturn(List.of());

        List<Product> result = productService.getAllActiveProductsBySection(section);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNewProductsExist_whenGetMainPhotosNewProducts_thenReturnPhotoUrls() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .mainPhotoUrl("photo1.jpg")
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 2")
                .mainPhotoUrl("photo2.jpg")
                .build();

        List<Product> newProducts = List.of(product1, product2);

        when(productRepository.findAllByOrderByAddedOnDesc()).thenReturn(newProducts);

        List<String> photoUrls = productService.getMainPhotosNewProducts();

        assertNotNull(photoUrls);
        assertEquals(2, photoUrls.size());
        assertTrue(photoUrls.contains("photo1.jpg") && photoUrls.contains("photo2.jpg"));
    }

    @Test
    void givenNewProductsDoNotExist_whenGetMainPhotosNewProducts_thenReturnEmptyList() {

        when(productRepository.findAllByOrderByAddedOnDesc()).thenReturn(List.of());

        List<String> photoUrls = productService.getMainPhotosNewProducts();
        assertNotNull(photoUrls);
        assertTrue(photoUrls.isEmpty());
    }

    @Test
    void givenBestSellersExist_whenGetBestSellers_thenReturnPhotoUrls() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Bestseller 1")
                .mainPhotoUrl("bestseller1.jpg")
                .itemsSold(100)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Bestseller 2")
                .mainPhotoUrl("bestseller2.jpg")
                .itemsSold(200)
                .build();

        List<Product> bestsellers = List.of(product2, product1);

        when(productRepository.findAllByOrderByItemsSoldDesc()).thenReturn(bestsellers);

        List<String> photoUrls = productService.getBestSellers();

        assertNotNull(photoUrls);
        assertEquals(2, photoUrls.size());
        assertEquals("bestseller2.jpg", photoUrls.get(0));
        assertEquals("bestseller1.jpg", photoUrls.get(1));
    }

    @Test
    void givenBestSellersDoNotExist_whenGetBestSellers_thenReturnEmptyList() {

        when(productRepository.findAllByOrderByItemsSoldDesc()).thenReturn(List.of());

        List<String> photoUrls = productService.getBestSellers();

        assertNotNull(photoUrls);
        assertTrue(photoUrls.isEmpty());
    }

    @Test
    void givenProductsExist_whenGetAll_thenReturnListOfProducts() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 1")
                .price(BigDecimal.valueOf(10))
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Product 2")
                .price(BigDecimal.valueOf(20))
                .build();

        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getListingTitle());
        assertEquals("Product 2", result.get(1).getListingTitle());
    }

    @Test
    void givenProductsDoNotExist_whenGetAll_thenReturnEmptyList() {

        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSufficientStock_whenDecreaseQuantityAndIncreaseItemsSold_thenDecreaseQuantityAndIncreaseItemsSold() {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .stockQuantity(10)
                .itemsSold(5)
                .active(true)
                .build();

        Map<Product, Integer> cartContent = new HashMap<>();
        cartContent.put(product, 3);

        productService.decreaseQuantityAndIncreaseItemsSold(cartContent);

        assertEquals(7, product.getStockQuantity());
        assertEquals(8, product.getItemsSold());
        assertTrue(product.isActive());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void givenStockDepletes_whenDecreaseQuantityAndIncreaseItemsSold_thenProductIsDeactivated() {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .stockQuantity(3)
                .itemsSold(2)
                .active(true)
                .build();

        Map<Product, Integer> cartContent = new HashMap<>();
        cartContent.put(product, 3);

        productService.decreaseQuantityAndIncreaseItemsSold(cartContent);

        assertEquals(0, product.getStockQuantity());
        assertEquals(5, product.getItemsSold());
        assertFalse(product.isActive());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void givenInsufficientStock_whenDecreaseQuantityAndIncreaseItemsSold_thenNoChangesMade() {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .stockQuantity(2)
                .itemsSold(1)
                .active(true)
                .build();


        Map<Product, Integer> cartContent = new HashMap<>();
        cartContent.put(product, 5);

        productService.decreaseQuantityAndIncreaseItemsSold(cartContent);

        assertEquals(2, product.getStockQuantity());
        assertEquals(1, product.getItemsSold());
        assertTrue(product.isActive());
        verify(productRepository, never()).save(product);
    }

    @Test
    void givenValidProductRequest_whenCreateProduct_thenProductIsCreatedAndSaved() throws IOException {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .listingTitle("Product 1")
                .description("Description of product")
                .price(BigDecimal.valueOf(100))
                .productSection(ProductSection.CUPS_AND_MUGS)
                .stockQuantity(10)
                .build();

        MultipartFile mainPhoto = mock(MultipartFile.class);
        List<MultipartFile> additionalPhotos = List.of(mock(MultipartFile.class), mock(MultipartFile.class));

        String mainPhotoUrl = "http://cloudinary.com/main-photo-url";
        String additionalPhotoUrl1 = "http://cloudinary.com/additional-photo-1";
        String additionalPhotoUrl2 = "http://cloudinary.com/additional-photo-2";

        when(cloudinaryService.uploadPhoto(mainPhoto, "products/main/")).thenReturn(mainPhotoUrl);
        when(cloudinaryService.uploadPhoto(additionalPhotos.get(0), "products/additional/")).thenReturn(additionalPhotoUrl1);
        when(cloudinaryService.uploadPhoto(additionalPhotos.get(1), "products/additional/")).thenReturn(additionalPhotoUrl2);

        when(productRepository.save(any(Product.class))).thenReturn(null);

        productService.createProduct(addProductRequest, mainPhoto, additionalPhotos);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(productCaptor.capture());

        Product capturedProduct = productCaptor.getValue();
        assertNotNull(capturedProduct);
        assertEquals("Product 1", capturedProduct.getListingTitle());
        assertEquals("Description of product", capturedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(100), capturedProduct.getPrice());
        assertEquals("http://cloudinary.com/main-photo-url", capturedProduct.getMainPhotoUrl());
        assertEquals(List.of("http://cloudinary.com/additional-photo-1", "http://cloudinary.com/additional-photo-2"), capturedProduct.getAdditionalPhotosUrls());
        assertEquals(ProductSection.CUPS_AND_MUGS, capturedProduct.getProductSection());
        assertTrue(capturedProduct.isActive());
        assertEquals(10, capturedProduct.getStockQuantity());
        verify(productRepository, times(1)).save(productCaptor.capture());
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeCups_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.CUPS_AND_MUGS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("C&M-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeCups_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.CUPS_AND_MUGS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("C&M-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("C&M-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeTiles_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TILES)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TILE-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTiles_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TILES)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TILE-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeHome_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.HOME_DECOR)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("HOME-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHome_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.HOME_DECOR)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("HOME-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeSet_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.SETS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("SET-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeSet_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.SETS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("SET-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeTea_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TEAPOTS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TEA-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeTea_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TEAPOTS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TEA-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeTray_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TRAYS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TPW-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeTray_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.TRAYS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("TPW-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodePitcher_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.PITCHERS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("PJB-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomePitcher_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.PITCHERS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("PJB-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeSugar_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.SUGAR_BOWLS)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("SCC-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeSugar_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.SUGAR_BOWLS)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("SCC-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeHallo_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.HALLOWEEN)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("HAllO-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeHallo_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.HALLOWEEN)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("HAllO-1012", productCode);
    }

    @Test
    void givenNoPreviousProduct_whenGenerateProductCodeAll_thenReturnCorrectCode() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.ALL)
                .build();

        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(null);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("ALL-1011", productCode);
    }

    @Test
    void givenPreviousProduct_whenGenerateProductCodeTilesHomeAll_thenReturnIncrementedCode() {
        AddProductRequest addProductRequest = AddProductRequest.builder()
                .productSection(ProductSection.ALL)
                .build();

        Product lastProduct = Product.builder()
                .productCode("TILE-1011")
                .build();
        when(productRepository.findTopByOrderByAddedOnDesc()).thenReturn(lastProduct);

        String productCode = productService.generateProductCode(addProductRequest);

        assertEquals("ALL-1012", productCode);
    }



}
