package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.ContactMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferredContactModeUpdateRequestDto {

    @NotNull(message = "Preferred contact mode is mandatory")
    private ContactMode preferredContactMode;

}