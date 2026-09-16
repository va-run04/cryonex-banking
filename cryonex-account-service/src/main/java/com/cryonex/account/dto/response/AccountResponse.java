package com.cryonex.account.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {
    private String accountId;

    private String accountNumber;

    private String ifsc;

    private String accountStatus;

}
