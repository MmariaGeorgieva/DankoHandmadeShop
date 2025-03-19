package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.model.Newsletter;
import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.web.dto.EmailRequest;
import com.danko.danko_handmade.web.dto.EmailResponse;
import com.danko.danko_handmade.web.dto.NewsletterRequest;
import com.danko.danko_handmade.web.dto.NewsletterResponse;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {

        Email email = emailService.sendEmail(emailRequest);

        EmailResponse response = DtoMapper.mapEmailToEmailResponse(email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/newsletter")
    public ResponseEntity<NewsletterResponse> sendNewsletter(@RequestBody NewsletterRequest newsLetterRequest) {

        Newsletter newsletter = emailService.sendNewsletter(newsLetterRequest);

        NewsletterResponse response = DtoMapper.mapNewsletterToNewsletterResponse(newsletter);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


}
