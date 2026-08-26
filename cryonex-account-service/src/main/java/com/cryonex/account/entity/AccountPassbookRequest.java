package com.cryonex.account.entity;

import com.cryonex.account.enums.DeliveryMode;
import com.cryonex.account.enums.PassbookRequestType;
import com.cryonex.account.enums.PassbookStatus;
import com.cryonex.account.enums.RequestMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "account_passbook_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountPassbookRequest extends BaseEntity {

    @Id
    @Column(name = "passbook_request_id", length = 20)
    private String passbookRequestId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "customer_id", length = 20, nullable = false)
    private String customerId;

    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", length = 20, nullable = false)
    private PassbookRequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_mode", length = 30, nullable = false)
    private RequestMode requestMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 30, nullable = false)
    private DeliveryMode deliveryMode;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "branch_code", length = 20)
    private String branchCode;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", length = 30, nullable = false)
    private PassbookStatus requestStatus;

    @Column(name = "courier_tracking_number", length = 100)
    private String courierTrackingNumber;

    @Column(name = "dispatch_date")
    private LocalDate dispatchDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "remarks", length = 500)
    private String remarks;

}