package com.danko.danko_handmade.email;

import com.danko.danko_handmade.email.model.*;
import com.danko.danko_handmade.email.repository.ContactFormEmailRepository;
import com.danko.danko_handmade.email.repository.EmailRepository;
import com.danko.danko_handmade.email.repository.NewsletterRepository;
import com.danko.danko_handmade.email.service.EmailService;
import com.danko.danko_handmade.user.model.User;
import com.danko.danko_handmade.web.dto.ContactShopRequest;
import com.danko.danko_handmade.web.dto.EmailRequest;
import com.danko.danko_handmade.web.dto.NewsletterRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUTest {

    @Mock
    private MailSender mailSender;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private NewsletterRepository newsletterRepository;
    @Mock
    private ContactFormEmailRepository contactFormEmailRepository;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        reset(mailSender, javaMailSender, emailRepository, newsletterRepository, contactFormEmailRepository);
    }

    @Test
    void whenSendEmailSucceeds_thenEmailIsSent() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("email@email.com")
                .build();

        EmailRequest emailRequest = new EmailRequest(user, "subject", "body");

        Email email = Email.builder()
                .userId(user.getId())
                .userEmail(user.getEmail())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .status(EmailStatus.SUCCEEDED)
                .build();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email result = emailService.sendEmail(emailRequest);

        assertEquals(email.getStatus(), result.getStatus());
        assertEquals(user.getEmail(), result.getUserEmail());
        assertEquals(emailRequest.getSubject(), result.getSubject());
        assertEquals(emailRequest.getBody(), result.getBody());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void whenSendEmailFails_thenEmailStatusIsFailed() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("email@email.com")
                .build();

        EmailRequest emailRequest = new EmailRequest(user, "subject", "body");

        Email email = Email.builder()
                .userId(user.getId())
                .userEmail(user.getEmail())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .status(EmailStatus.FAILED)
                .build();

        doAnswer(invocation -> {
            throw new RuntimeException("Email server down");
        }).when(mailSender).send(any(SimpleMailMessage.class));
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email result = emailService.sendEmail(emailRequest);

        assertEquals(EmailStatus.FAILED, result.getStatus());
        System.out.println("Did mailSender.send() execute?");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void whenSendNewsletterSucceeds_thenNewsletterIsSent() throws MessagingException {
        String[] recipients = {"recipient1@example.com", "recipient2@example.com"};
        NewsletterRequest newsletterRequest = new NewsletterRequest(recipients, "Newsletter Subject", "Newsletter Body");

        Newsletter newsletter = Newsletter.builder()
                .newsletterContactList(List.of(recipients))
                .subject(newsletterRequest.getSubject())
                .body(newsletterRequest.getBody())
                .status(NewsletterStatus.SUCCEEDED)
                .build();

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(newsletterRepository.save(any(Newsletter.class))).thenReturn(newsletter);

        Newsletter result = emailService.sendNewsletter(newsletterRequest);

        assertEquals(NewsletterStatus.SUCCEEDED, result.getStatus());
        assertEquals(newsletterRequest.getSubject(), result.getSubject());
        assertEquals(newsletterRequest.getBody(), result.getBody());
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(newsletterRepository, times(1)).save(any(Newsletter.class));
    }


    @Test
    void whenSendNewsletterFails_thenNewsletterStatusIsFailed() {
        String[] recipients = {"recipient1@example.com", "recipient2@example.com"};
        NewsletterRequest newsletterRequest = new NewsletterRequest(recipients, "Newsletter Subject", "Newsletter Body");

        Newsletter newsletter = Newsletter.builder()
                .newsletterContactList(List.of(recipients))
                .subject(newsletterRequest.getSubject())
                .body(newsletterRequest.getBody())
                .status(NewsletterStatus.FAILED)
                .build();

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(newsletterRepository.save(any(Newsletter.class))).thenReturn(newsletter);

        Newsletter result = emailService.sendNewsletter(newsletterRequest);

        assertEquals(NewsletterStatus.FAILED, result.getStatus());
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(newsletterRepository, times(1)).save(any(Newsletter.class));
    }

    @Test
    void whenSendEmailThroughContactFormIsCalled_thenContactFormIsSent() throws MessagingException {

        ContactShopRequest contactShopRequest = ContactShopRequest.builder()
                .name("Test name")
                .email("test@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        emailService.sendEmailThroughContactForm(contactShopRequest);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        verify(contactFormEmailRepository, times(1)).save(argThat(contactFormEmail ->
                contactFormEmail.getName().equals(contactShopRequest.getName()) &&
                        contactFormEmail.getEmail().equals(contactShopRequest.getEmail()) &&
                        contactFormEmail.getSubject().equals(contactShopRequest.getSubject()) &&
                        contactFormEmail.getBody().equals(contactShopRequest.getBody()) &&
                        contactFormEmail.getCreatedOn() != null
        ));
    }

    @Test
    void whenGetContactFormEmailsIsCalled_thenCorrectListIsReceived() {
        List<ContactFormEmail> contactFormEmails = List.of(new ContactFormEmail(), new ContactFormEmail());
        when(contactFormEmailRepository.findAllByOrderByCreatedOnDesc()).thenReturn(contactFormEmails);
        List<ContactFormEmail> result = emailService.getContactFormEmails();
        assertEquals(2, result.size());
    }
}
