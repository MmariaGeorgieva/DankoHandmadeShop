package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.email.model.EmailStatus;
import com.danko.danko_handmade.email.model.NewsletterStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsletterResponse {

    private String subject;

    private LocalDateTime createdOn;

    protected NewsletterStatus status;
}
