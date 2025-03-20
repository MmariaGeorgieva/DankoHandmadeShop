package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EmailRequest {

    @NotNull
    private User user;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
