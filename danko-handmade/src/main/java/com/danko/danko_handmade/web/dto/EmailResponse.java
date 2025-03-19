package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.email.model.EmailStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailResponse {

    private String subject;

    private LocalDateTime createdOn;

    protected EmailStatus status;
}
