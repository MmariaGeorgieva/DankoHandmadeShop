package com.danko.danko_handmade.email.service;

import com.danko.danko_handmade.product.service.ProductService;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.NewsletterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.thymeleaf.context.Context;

@Service
public class NewsletterScheduler {

    private final EmailService emailService;
    private final UserService userService;
    private final ProductService productService;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public NewsletterScheduler(EmailService emailService, UserService userService, ProductService productService, SpringTemplateEngine thymeleafTemplateEngine) {
        this.emailService = emailService;
        this.userService = userService;
        this.productService = productService;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

//    @Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(cron = "0 0 12 * * MON")
    public void sendWeeklyNewsletter() {
        List<String> mainPhotoUrlsNewProducts = productService.getMainPhotosNewProducts();
        List<String> bestSellingProductsUrls = productService.getBestSellers();
        bestSellingProductsUrls = bestSellingProductsUrls.stream().limit(5).toList();
        mainPhotoUrlsNewProducts = mainPhotoUrlsNewProducts.stream().limit(5).toList();

        Map<String, Object> model = new HashMap<>();
        model.put("newProducts", mainPhotoUrlsNewProducts);
        model.put("bestSellers", bestSellingProductsUrls);

        String emailBody = thymeleafTemplateEngine
                .process("newsletter", new Context(Locale.getDefault(), model));

        String[] newsletterContactList = userService
                .getAllUsers()
                .stream()
                .filter(User::isSubscribedToBulletin)
                .map(User::getEmail)
                .toArray(String[]::new);

        NewsletterRequest newsletterRequest = new NewsletterRequest();
        newsletterRequest.setSubject("Weekly Newsletter");
        newsletterRequest.setBody(emailBody);
        newsletterRequest.setNewsletterContactList(newsletterContactList);

        emailService.sendNewsletter(newsletterRequest);
    }

}
