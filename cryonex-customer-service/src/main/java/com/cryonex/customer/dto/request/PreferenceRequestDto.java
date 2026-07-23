package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.CommunicationMode;
import com.cryonex.customer.enums.Language;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceRequestDto {

    @NotNull(message = "Language is mandatory")
    private Language language;

    @NotNull(message = "Communication mode is mandatory")
    private CommunicationMode communicationMode;

    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean marketingEnabled;

}