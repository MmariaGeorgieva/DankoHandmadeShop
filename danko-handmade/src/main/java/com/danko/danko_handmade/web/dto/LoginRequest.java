package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 25, message = "Username must be between 4 and 25 characters")
    private String username;

    @NotBlank(message = "Password field must not be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;
}
