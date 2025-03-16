package com.danko.danko_handmade.web.dto.mapper;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DtoMapper {

    public static EditProductRequest mapProductToProductEditRequest(Product product) {

        return EditProductRequest.builder()
                .listingTitle(product.getListingTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .productSection(product.getProductSection())
                .build();
    }
}
