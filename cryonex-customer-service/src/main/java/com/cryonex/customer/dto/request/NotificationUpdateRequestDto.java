package com.cryonex.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationUpdateRequestDto {

    private Boolean emailEnabled;
    private Boolean smsEnabled;

}