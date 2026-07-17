package com.cryonex.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUpdateRequestDto {

    private String doorNumber;
    private String street;
    private String area;
    private String city;
    private String district;
    private String state;
    private String country;
    private String postalCode;
    private boolean primary;

}