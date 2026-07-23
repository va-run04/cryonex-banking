package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.CommunicationMode;
import com.cryonex.customer.enums.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceUpdateRequestDto {

    private Language language;
    private CommunicationMode communicationMode;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean marketingEnabled;

}