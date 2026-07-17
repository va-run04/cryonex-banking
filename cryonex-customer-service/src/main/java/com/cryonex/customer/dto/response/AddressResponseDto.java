package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.AddressType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {

    private String addressId;
    private AddressType addressType;
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