package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.ContactMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactResponseDto {

    private String contactId;
    private String mobileNumber;
    private String alternateMobile;
    private String email;
    private String landline;
    private ContactMode preferredContactMode;

}