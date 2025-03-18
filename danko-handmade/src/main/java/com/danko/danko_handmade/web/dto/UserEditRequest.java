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

    @Email
    private String email;

    @URL(message = "Requires correct web link format")
    private String profilePicture;

    private String packageRecipientName;

    private Country country;

    private String city;

    private String postalCode;

    private String street;

    private String streetNumber;

    private String phoneNumber;
}
