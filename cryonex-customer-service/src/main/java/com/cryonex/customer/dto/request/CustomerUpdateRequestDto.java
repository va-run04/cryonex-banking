package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.MaritalStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerUpdateRequestDto {

    private String firstName;
    private String middleName;
    private String lastName;
    private MaritalStatus maritalStatus;
    private String occupation;
    private BigDecimal annualIncome;
    private String nationality;

}