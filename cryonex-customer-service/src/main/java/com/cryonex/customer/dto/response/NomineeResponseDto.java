package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.Relationship;
import com.cryonex.customer.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NomineeResponseDto {

    private String nomineeId;
    private String nomineeName;
    private Relationship relationship;
    private LocalDate dob;
    private String mobile;
    private BigDecimal sharePercentage;
    private VerificationStatus verificationStatus;

}