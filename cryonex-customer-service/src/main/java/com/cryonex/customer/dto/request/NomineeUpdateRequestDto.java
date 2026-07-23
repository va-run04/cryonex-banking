package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.Relationship;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NomineeUpdateRequestDto {

    private String nomineeName;
    private Relationship relationship;
    private LocalDate dob;
    private String mobile;
    private BigDecimal sharePercentage;

}