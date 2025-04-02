package com.danko.danko_handmade.email;

import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.email.service.NewsletterScheduler;
import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.thymeleaf.context.Context;

@ExtendWith(MockitoExtension.class)
public class NewsletterSchedulerUTest {
    @Mock
    private EmailService emailService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @Mock
    private SpringTemplateEngine thymeleafTemplateEngine;

    @InjectMocks
    private NewsletterScheduler newsletterScheduler;

    @Test
    void testSendWeeklyNewsletter() {
        List<String> newProductPhotos = List.of("newProduct1.jpg", "newProduct2.jpg", "newProduct3.jpg", "newProduct4.jpg", "newProduct5.jpg");
        List<String> bestSellingProductPhotos = List.of("bestSeller1.jpg", "bestSeller2.jpg", "bestSeller3.jpg", "bestSeller4.jpg", "bestSeller5.jpg");

        when(productService.getMainPhotosNewProducts()).thenReturn(newProductPhotos);
        when(productService.getBestSellers()).thenReturn(bestSellingProductPhotos);

        String expectedEmailBody = "<html><body>Test Email Body</body></html>";
        when(thymeleafTemplateEngine.process(anyString(), any(Context.class))).thenReturn(expectedEmailBody);

        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setSubscribedToBulletin(true);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setSubscribedToBulletin(false);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        newsletterScheduler.sendWeeklyNewsletter();

        verify(emailService, times(1)).sendNewsletter(argThat(newsletterRequest ->
                newsletterRequest.getSubject().equals("Weekly Newsletter") &&
                        newsletterRequest.getBody().equals(expectedEmailBody) &&
                        newsletterRequest.getNewsletterContactList().length == 1 &&
                        newsletterRequest.getNewsletterContactList()[0].equals("user1@example.com")
        ));

        verify(productService, times(1)).getMainPhotosNewProducts();
        verify(productService, times(1)).getBestSellers();
    }
}
