package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.CustomerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerStatusUpdateRequestDto {

    @NotNull(message = "Status is mandatory")
    private CustomerStatus status;

}