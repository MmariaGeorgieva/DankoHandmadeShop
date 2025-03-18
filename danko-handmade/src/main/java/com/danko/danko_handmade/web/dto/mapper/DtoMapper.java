package com.danko.danko_handmade.web.dto.mapper;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

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

    public static UserEditRequest mapUserToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .packageRecipientName(user.getPackageRecipientName())
                .country(user.getCountry())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .street(user.getStreet())
                .streetNumber(user.getStreetNumber())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
