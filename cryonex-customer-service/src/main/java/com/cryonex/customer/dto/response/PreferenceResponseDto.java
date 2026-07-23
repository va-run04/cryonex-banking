package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.CommunicationMode;
import com.cryonex.customer.enums.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceResponseDto {

    private String preferenceId;
    private Language language;
    private CommunicationMode communicationMode;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean marketingEnabled;

}