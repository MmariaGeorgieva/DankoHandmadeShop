package com.danko.danko_handmade.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 25, message = "Username must be between 4 and 25 characters")
    private String username;

    @NotBlank(message = "Password field must not be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    @NotNull(message = "Email field must not be empty")
    @Email(message = "Please provide a valid email address")
    private String email;
}
