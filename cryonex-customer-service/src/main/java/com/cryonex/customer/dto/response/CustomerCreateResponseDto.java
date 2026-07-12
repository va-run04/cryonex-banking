package com.cryonex.customer.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerCreateResponseDto {

    private String customerId;
    private String cifNumber;
    private LocalDateTime createdDate;

}