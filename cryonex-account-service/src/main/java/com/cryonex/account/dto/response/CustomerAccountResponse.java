package com.cryonex.account.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAccountResponse {
    private String accountNumber;
    private String accountType;
    private String accountStatus;
}