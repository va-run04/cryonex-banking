package com.cryonex.account.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class AccountTypeResponse {

    private Long typeId;
    private String typeCode;
    private String typeName;
    private String description;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;
    private BigDecimal withdrawalLimit;
    private BigDecimal dailyTransferLimit;
    private BigDecimal maintenanceCharge;
    private String chequeBookAvailable;
    private String debitCardAvailable;
    private String passbookAvailable;
    private String overdraftAllowed;
    private String accountStatus;

}

