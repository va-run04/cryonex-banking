package com.cryonex.account.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSearchResponse {
    private String accountId;
    private String customerId;
    private String accountNumber;
    private String accountType;
    private String branchCode;
    private String accountStatus;
}
