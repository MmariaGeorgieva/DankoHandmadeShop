package com.danko.danko_handmade.web.dto.mapper;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EditProductRequest mapProductToProductEditRequest(Product product) {
        return EditProductRequest.builder()
                .listingTitle(product.getListingTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .productSection(product.getProductSection())
                .stockQuantity(product.getStockQuantity())
                .mainPhotoUrl(product.getMainPhotoUrl())
                .additionalPhotos(product.getAdditionalPhotos())
                .weight(product.getWeight())
                .build();
    }
}
