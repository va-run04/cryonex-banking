package com.cryonex.account.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountBalanceResponse {
    private String accountNumber;
    private BigDecimal availableBalance;
    private BigDecimal ledgerBalance;
}