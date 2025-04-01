package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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
