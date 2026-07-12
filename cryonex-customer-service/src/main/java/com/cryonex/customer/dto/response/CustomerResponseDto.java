package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.CustomerStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDto {

    private String customerId;
    private String cifNumber;
    private String customerName;
    private String mobile;
    private String email;
    private CustomerStatus status;

}