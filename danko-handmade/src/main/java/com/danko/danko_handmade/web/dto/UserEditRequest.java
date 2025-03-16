package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.address.model.Address;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@Builder
public class UserEditRequest {

    @Size(max = 20, message = "First name must be 20 symbols max")
    private String firstName;

    @Size(max = 20, message = "Last name must be 20 symbols max")
    private String lastName;

    private String phone;

    @URL(message = "Requires correct web link format")
    private String profilePicture;
}
