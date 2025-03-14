package com.danko.danko_handmade.email.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private UUID userId;

    private boolean isDeleted;

    @Column(nullable = false)
    private String body;

}
