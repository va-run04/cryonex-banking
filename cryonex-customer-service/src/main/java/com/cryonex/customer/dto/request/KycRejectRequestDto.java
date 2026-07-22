package com.cryonex.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycRejectRequestDto {

    @NotBlank(message = "Rejection reason is mandatory")
    private String reason;

    @NotBlank(message = "Verified by is mandatory")
    private String verifiedBy;

}