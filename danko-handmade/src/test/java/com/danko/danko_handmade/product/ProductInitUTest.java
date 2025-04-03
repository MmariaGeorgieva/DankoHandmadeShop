package com.danko.danko_handmade.product;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import com.danko.danko_handmade.product.service.ProductInit;
import com.danko.danko_handmade.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}