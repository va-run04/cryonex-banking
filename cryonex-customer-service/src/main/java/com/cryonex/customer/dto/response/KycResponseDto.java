package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.KycStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KycResponseDto {

    private String customerId;
    private Boolean panVerified;
    private Boolean aadhaarVerified;
    private KycStatus kycStatus;
    private String verifiedBy;
    private LocalDateTime verifiedDate;

}