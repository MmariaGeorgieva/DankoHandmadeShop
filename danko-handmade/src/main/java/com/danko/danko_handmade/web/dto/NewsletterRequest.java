package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterRequest {

    @NotNull
    private String[] newsletterContactList;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
