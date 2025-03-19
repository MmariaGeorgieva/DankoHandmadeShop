package com.danko.danko_handmade.email.service;

import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.model.EmailStatus;
import com.danko.danko_handmade.email.model.Newsletter;
import com.danko.danko_handmade.email.model.NewsletterStatus;
import com.danko.danko_handmade.email.repository.EmailRepository;
import com.danko.danko_handmade.email.repository.NewsletterRepository;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.user.service.UserService;
import com.danko.danko_handmade.web.dto.EmailRequest;
import com.danko.danko_handmade.web.dto.NewsletterRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final NewsletterRepository newsletterRepository;
    private final UserService userService;

    @Autowired
    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, EmailRepository emailRepository, NewsletterRepository newsletterRepository, UserService userService) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
        this.newsletterRepository = newsletterRepository;
        this.userService = userService;
    }

    public Email sendEmail(EmailRequest emailRequest) {

        UUID userId = emailRequest.getUserId();
        User user = userService.getById(userId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());

        Email email = Email.builder()
                .userId(userId)
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .userEmail(user.getEmail())
                .createdOn(LocalDateTime.now())
                .build();

        try {
            mailSender.send(message);
            email.setStatus(EmailStatus.SUCCEEDED);
        } catch (Exception e) {
            email.setStatus(EmailStatus.FAILED);
            log.warn("Failed to send email due to %s".formatted(e.getMessage()));
        }
        return emailRepository.save(email);
    }

    public Newsletter sendNewsletter(NewsletterRequest newsletterRequest) {

        Newsletter newsletter = Newsletter.builder()
                .newsletterContactList(List.of(newsletterRequest.getNewsletterContactList()))
                .subject(newsletterRequest.getSubject())
                .body(newsletterRequest.getBody())
                .createdOn(LocalDateTime.now())
                .build();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(newsletterRequest.getNewsletterContactList());
            helper.setSubject(newsletterRequest.getSubject());
            helper.setText(newsletterRequest.getBody(), true);

            javaMailSender.send(message);
            newsletter.setStatus(NewsletterStatus.SUCCEEDED);
            return newsletterRepository.save(newsletter);
        } catch (MessagingException e) {
            newsletter.setStatus(NewsletterStatus.FAILED);
            log.warn("Failed to send newsletter due to %s".formatted(e.getMessage()));
            return newsletterRepository.save(newsletter);
        }
    }
}
