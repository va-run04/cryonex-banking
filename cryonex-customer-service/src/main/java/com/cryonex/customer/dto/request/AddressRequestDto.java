package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {

    @NotNull(message = "Address type is mandatory")
    private AddressType addressType;

    private String doorNumber;

    private String street;

    private String area;

    @NotBlank(message = "City is mandatory")
    private String city;

    private String district;

    @NotBlank(message = "State is mandatory")
    private String state;

    @NotNull(message = "Country is mandatory")
    private String country;

    @NotBlank(message = "Postal code is mandatory")
    @Pattern(regexp = "\\d{6}", message = "Postal code must be 6 digits")
    private String postalCode;

    private boolean primary;

}