package com.danko.danko_handmade.product.service;

import com.danko.danko_handmade.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private static int start = 1000;

//    @Transactional
//    public Product createProduct(Product product) {
//
//        String productCode = generateProductCode(product);
//
//        product.setProductCode(productCode);
//
//
//        return productRepository.save(product);
//    }
//
//    private String generateProductCode(Product product) {
//
//        String prefix = product.getProductSection().toString().substring(0, 4);
//        int suffix = start++;
//        StringBuilder sb = new StringBuilder();
//        sb.append(prefix).append("-").append(suffix);
//        return sb.toString();
//}
}
