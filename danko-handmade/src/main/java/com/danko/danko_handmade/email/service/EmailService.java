package com.danko.danko_handmade.email.service;

import com.danko.danko_handmade.email.model.Email;
import com.danko.danko_handmade.email.repository.EmailRepository;
import com.danko.danko_handmade.web.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    private final MailSender mailSender;
    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(MailSender mailSender, EmailRepository emailRepository) {
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
    }

    public Email sendEmail(EmailRequest emailRequest) {

        UUID userId = emailRequest.getUserId();

        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email.getContactInfo());
//        message.setSubject(notificationRequest.getSubject());
//        message.setText(notificationRequest.getBody());
//
//        Notification notification = Notification.builder()
//                .userId(userId)
//                .subject(notificationRequest.getSubject())
//                .body(notificationRequest.getBody())
//                .createdOn(LocalDateTime.now())
//                .type(NotificationType.EMAIL)
//                .isDeleted(false)
//                .build();
//
//        try {
//            mailSender.send(message);
//            notification.setStatus(NotificationStatus.SUCCEEDED);
//        } catch (Exception e) {
//            notification.setStatus(NotificationStatus.FAILED);
//            log.warn("Failed to send notification due to %s".formatted(e.getMessage()));
//        }
//        return emailRepository.save(email);
        return null;
    }

}
