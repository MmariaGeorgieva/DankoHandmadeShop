package com.danko.danko_handmade.web.dto;

import com.danko.danko_handmade.address.model.Country;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAddressRequest {

    @NotNull
    private String recipientName;

    @NotNull
    private Country country;

    @NotNull
    private String city;

    @NotNull
    private String postalCode;

    @NotNull
    private String street;

    @NotNull
    private String streetNumber;

    private String phoneNumber;
}
