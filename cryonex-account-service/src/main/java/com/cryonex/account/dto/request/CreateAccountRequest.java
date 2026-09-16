package com.cryonex.account.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Customer Id is mandatory")
    private String customerId;

    @NotBlank(message = "Account type is mandatory")
    private String accountType;

    @NotBlank(message = "Branch Code is mandatory")
    private String branchCode;

    @NotBlank(message = "Currency is mandatory")
    private String currency;

    @NotNull(message = "Initial deposit is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial deposit must be greator than zero")
    private BigDecimal initialDeposit;

}
