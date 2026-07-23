package com.cryonex.customer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketingUpdateRequestDto {

    @NotNull(message = "Marketing consent value is mandatory")
    private Boolean marketingEnabled;

}