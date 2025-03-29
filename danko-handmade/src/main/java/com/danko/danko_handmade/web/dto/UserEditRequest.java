package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.user.model.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class UserEditRequest {

    @Size(max = 20, message = "First name must be 20 symbols max")
    private String firstName;

    @Size(max = 20, message = "Last name must be 20 symbols max")
    private String lastName;

    @Email(message = "Must be a well-formed email address")
    private String email;

    @URL(message = "Requires correct web link format")
    private String profilePicture;

    @Size(max = 30, message = "Recipient name must be 30 symbols max")
    private String recipientName;

    private Country country;

    @Size(max = 30, message = "City name must be 30 symbols max")
    private String city;

    @Size(max = 15, message = "Postal code must be 15 symbols max")
    private String postalCode;

    @Size(max = 30, message = "Street name must be 30 symbols max")
    private String street;

    @Size(max = 10, message = "Street number must be 10 symbols max")
    private String streetNumber;

    @Size(max = 15, message = "Phone number must be 15 symbols max")
    private String phoneNumber;
}
