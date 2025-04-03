package com.danko.danko_handmade.web.mapper;

import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.model.EmailStatus;
import com.danko.danko_handmade.email.model.Newsletter;
import com.danko.danko_handmade.email.model.NewsletterStatus;
import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.user.model.Country;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.web.dto.EmailResponse;
import com.danko.danko_handmade.web.dto.NewsletterResponse;
import com.danko.danko_handmade.web.dto.UserEditRequest;
import com.danko.danko_handmade.web.dto.EditProductRequest;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenHappyPath_whenUserToUserEditRequest() {
        User user = User.builder()
                .firstName("Daniel")
                .lastName("Nikolov")
                .email("danko@mail")
                .profilePicture("picture.jpg")
                .recipientName("Daniel Nikolov")
                .country(Country.BULGARIA)
                .city("Pleven")
                .postalCode("5800")
                .street("First Street")
                .streetNumber("5")
                .phoneNumber("123456789")
                .build();
        UserEditRequest resultDto = DtoMapper.mapUserToUserEditRequest(user);
        assertEquals(user.getFirstName(), resultDto.getFirstName());
        assertEquals(user.getLastName(), resultDto.getLastName());
        assertEquals(user.getEmail(), resultDto.getEmail());
        assertEquals(user.getProfilePicture(), resultDto.getProfilePicture());
        assertEquals(user.getRecipientName(), resultDto.getRecipientName());
        assertEquals(user.getCountry().toString(), resultDto.getCountry().toString());
        assertEquals(user.getCity(), resultDto.getCity());
        assertEquals(user.getPostalCode(), resultDto.getPostalCode());
        assertEquals(user.getStreet(), resultDto.getStreet());
        assertEquals(user.getStreetNumber(), resultDto.getStreetNumber());
        assertEquals(user.getPhoneNumber(), resultDto.getPhoneNumber());
    }

    @Test
    void givenHappyPath_whenProductToEditProductRequest() {
        Product product = Product.builder()
                .listingTitle("My mug")
                .description("Mu nice mug")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(5)
                .active(true)
                .productSection(ProductSection.CUPS_AND_MUGS)
                .build();

        EditProductRequest resultDto = DtoMapper.mapProductToProductEditRequest(product);
        assertEquals(product.getListingTitle(), resultDto.getListingTitle());
        assertEquals(product.getDescription(), resultDto.getDescription());
        assertEquals(product.getPrice(), resultDto.getPrice());
        assertEquals(product.getStockQuantity(), resultDto.getStockQuantity());
        assertEquals(product.isActive(), resultDto.isActive());
        assertEquals(product.getProductSection(), resultDto.getProductSection());
    }

    @Test
    void givenHappyPath_whenEmailToEmailResponse() {
        Email email = Email.builder()
                .subject("Hello World")
                .status(EmailStatus.SUCCEEDED)
                .build();

        EmailResponse resultDto = DtoMapper.mapEmailToEmailResponse(email);
        assertEquals(email.getStatus(), resultDto.getStatus());
        assertEquals(email.getSubject(), resultDto.getSubject());
    }

    @Test
    void givenHappyPath_whenNewsletterToNewsletterResponse() {
        Newsletter newsletter = Newsletter.builder()
                .subject("Hello World")
                .status(NewsletterStatus.FAILED)
                .build();

        NewsletterResponse resultDto = DtoMapper.mapNewsletterToNewsletterResponse(newsletter);
        assertEquals(newsletter.getStatus(), resultDto.getStatus());
        assertEquals(newsletter.getSubject(), resultDto.getSubject());
    }
}
