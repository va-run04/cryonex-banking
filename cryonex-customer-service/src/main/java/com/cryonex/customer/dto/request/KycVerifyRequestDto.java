package com.cryonex.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycVerifyRequestDto {

    @NotNull(message = "PAN verification status is mandatory")
    private Boolean panVerified;

    @NotNull(message = "Aadhaar verification status is mandatory")
    private Boolean aadhaarVerified;

    @NotBlank(message = "Verified by is mandatory")
    private String verifiedBy;

}