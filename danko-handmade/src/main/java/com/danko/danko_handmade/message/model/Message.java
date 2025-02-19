package com.danko.danko_handmade.message.model;

import com.danko.danko_handmade.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "order")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    public void setSentAt() {
        if (this.sentAt == null) {
            this.sentAt = LocalDateTime.now();
        }
    }
}
