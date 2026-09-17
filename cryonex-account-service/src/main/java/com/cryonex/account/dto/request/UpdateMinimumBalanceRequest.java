package com.cryonex.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateMinimumBalanceRequest {
    @NotNull(message = "Minimum balance is mandatory")
    @DecimalMin(value = "0.0", message = "Minimum balance cannot be negative")
    private BigDecimal minimumBalance;
}