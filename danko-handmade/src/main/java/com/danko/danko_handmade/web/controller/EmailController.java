package com.danko.danko_handmade.web.controller;

import com.danko.danko_handmade.email.model.ContactFormEmail;
import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.model.Newsletter;
import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.web.dto.*;
import com.danko.danko_handmade.web.dto.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
