package com.danko.danko_handmade.web.dto.mapper;

import com.danko.danko_handmade.email.model.ContactFormEmail;
import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.model.Newsletter;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.web.dto.*;
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
                .recipientName(user.getRecipientName())
                .country(user.getCountry())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .street(user.getStreet())
                .streetNumber(user.getStreetNumber())
                .phoneNumber(user.getPhoneNumber())                .build();
    }

    public static EmailResponse mapEmailToEmailResponse(Email email) {
        return EmailResponse.builder()
                .subject(email.getSubject())
                .status(email.getStatus())
                .createdOn(email.getCreatedOn())
                .build();
    }

    public static NewsletterResponse mapNewsletterToNewsletterResponse(Newsletter newsletter) {
        return NewsletterResponse.builder()
                .subject(newsletter.getSubject())
                .status(newsletter.getStatus())
                .createdOn(newsletter.getCreatedOn())
                .build();
    }
}
