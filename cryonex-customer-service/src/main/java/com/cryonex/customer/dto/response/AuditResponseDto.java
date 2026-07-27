package com.cryonex.customer.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditResponseDto {

    private String auditId;
    private String action;
    private String performedBy;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdDate;

}