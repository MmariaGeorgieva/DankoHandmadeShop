package com.danko.danko_handmade.product;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.product.service.ProductInit;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.user.model.Role;
import com.danko.danko_handmade.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductInitUTest {

    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductInit productInit;

    @Test
    void givenProductsExist_whenRun_thenNoProductsAreCreated() throws Exception {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .listingTitle("Unique Handmade Fish Mug")
                .description("D - Unique Handmade Fish Mug")
                .productSection(ProductSection.CUPS_AND_MUGS)
                .productCode("C&M-1001")
                .build();

        when(productService.getAll()).thenReturn(List.of(product));

        productInit.run();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void givenProductsDoNotExist_whenRun_tenProductsAreCreated() throws Exception {

        when(productService.getAll()).thenReturn(new ArrayList<>());

        productInit.run();
        verify(productRepository, times(10)).save(any(Product.class));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(10)).save(productCaptor.capture());
        List<Product> createdProducts = productCaptor.getAllValues();
        assertEquals(10, createdProducts.size());
        assertEquals("C&M-1001", createdProducts.get(0).getProductCode());
        assertEquals("HAllO-1002", createdProducts.get(1).getProductCode());
        assertEquals("TEA-1003", createdProducts.get(2).getProductCode());
        assertEquals("SCC-1004", createdProducts.get(3).getProductCode());
        assertEquals("PJB-1005", createdProducts.get(4).getProductCode());
        assertEquals("SET-1006", createdProducts.get(5).getProductCode());
        assertEquals("TPW-1007", createdProducts.get(6).getProductCode());
        assertEquals("HOME-1008", createdProducts.get(7).getProductCode());
        assertEquals("TILE-1009", createdProducts.get(8).getProductCode());
        assertEquals("C&M-1010", createdProducts.get(9).getProductCode());
    }

//    @Test
//    public void testRun_ShouldAddProductsIfNoExistingProducts() throws Exception {
//
//        when(productRepository.findAll()).thenReturn(List.of());

//        when(productInit.readDescriptionFromFile("product1.txt")).thenReturn("Description 1");
//        when(productInit.readDescriptionFromFile("product2.txt")).thenReturn("Description 2");
//        when(productInit.readDescriptionFromFile("product3.txt")).thenReturn("Description 3");
//        when(productInit.readDescriptionFromFile("product4.txt")).thenReturn("Description 4");
//        when(productInit.readDescriptionFromFile("product5.txt")).thenReturn("Description 5");
//        when(productInit.readDescriptionFromFile("product6.txt")).thenReturn("Description 6");
//        when(productInit.readDescriptionFromFile("product7.txt")).thenReturn("Description 7");
//        when(productInit.readDescriptionFromFile("product8.txt")).thenReturn("Description 8");
//        when(productInit.readDescriptionFromFile("product9.txt")).thenReturn("Description 9");
//        when(productInit.readDescriptionFromFile("product10.txt")).thenReturn("Description 10");
//
//        Product product1 = createTestProduct("Unique Handmade Fish Mug", "Description 1",
//                "C&M-1001", ProductSection.CUPS_AND_MUGS);
//        Product product2 = createTestProduct("Handmade Grumpy Pumpkin Mug", "Description 2",
//                "HAllO-1002", ProductSection.HALLOWEEN);
//        Product product3 = createTestProduct("Dragonflies - Handmade Porcelain", "Description 3",
//                "TEA-1003", ProductSection.TEAPOTS);
//        Product product4 = createTestProduct("Handmade Stoneware Sugar Bowl", "Description 4",
//                "SCC-1004", ProductSection.SUGAR_BOWLS);
//        Product product5 = createTestProduct("Handmade Ceramic Water Pitcher", "Description 5",
//                "PJB-1005", ProductSection.PITCHERS);
//        Product product6 = createTestProduct("Hand-Painted Ceramic Christmas Gift Set", "Description 6",
//                "SET-1006", ProductSection.SETS);
//        Product product7 = createTestProduct("Handpainted Porcelain Tray with Mackerel Fish", "Description 7",
//                "TPW-1007", ProductSection.TRAYS);
//        Product product8 = createTestProduct("Decorative Blue and White Ceramic Teapot", "Description 8",
//                "HOME-1008", ProductSection.HOME_DECOR);
//        Product product9 = createTestProduct("Parque das Nações", "Description 9",
//                "TILE-1009", ProductSection.TILES);
//        Product product10 = createTestProduct("Hand-Painted Cat Mug", "Description 10",
//                "C&M-1010", ProductSection.CUPS_AND_MUGS);


//        when(productRepository.save(any(Product.class))).thenReturn(
//                product1,
//                product2,
//                product3,
//                product4,
//                product5,
//                product6,
//                product7,
//                product8,
//                product9,
//                product10
//        );

//        productInit.run();
//
//        verify(productRepository, times(10)).save(any(Product.class));
//    }
//
//    @Test
//    public void testRun_ShouldNotAddProductsIfThereAreExistingProducts() throws Exception {
//        // Мокваме, че вече има продукти в базата данни
//        when(productRepository.findAll()).thenReturn(List.of(new Product()));
//
//        // Извикваме метода
//        productInit.run();
//
//        // Проверяваме дали методът save() не е бил извикан, защото има съществуващи продукти
//        verify(productRepository, never()).save(any(Product.class));
//    }
//
//    private Product createTestProduct(String title, String description, String code, ProductSection section) {
//        return Product.builder()
//                .listingTitle(title)
//                .description(description)
//                .price(new BigDecimal("50"))
//                .productCode(code)
//                .mainPhotoUrl("https://example.com/photo.jpg")
//                .additionalPhotosUrls(List.of("https://example.com/photo1.jpg"))
//                .productSection(section)
//                .addedOn(LocalDateTime.now())
//                .stockQuantity(5)
//                .active(true)
//                .itemsSold(0)
//                .build();
//    }
}
