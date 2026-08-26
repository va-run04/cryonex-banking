package com.cryonex.account.entity;

import com.cryonex.account.enums.ChequeBookStatus;
import com.cryonex.account.enums.ChequeBookType;
import com.cryonex.account.enums.DeliveryMode;
import com.cryonex.account.enums.RequestMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "account_cheque_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountChequeBookRequest extends BaseEntity {

    @Id
    @Column(name = "cheque_book_id", length = 20)
    private String chequeBookId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "customer_id", length = 20, nullable = false)
    private String customerId;

    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "cheque_book_type", length = 20, nullable = false)
    private ChequeBookType chequeBookType;

    @Column(name = "leaves_count", nullable = false)
    private Integer leavesCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_mode", length = 30, nullable = false)
    private RequestMode requestMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 30, nullable = false)
    private DeliveryMode deliveryMode;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", length = 30, nullable = false)
    private ChequeBookStatus requestStatus;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "dispatched_date")
    private LocalDate dispatchedDate;

    @Column(name = "delivered_date")
    private LocalDate deliveredDate;

    @Column(name = "remarks", length = 500)
    private String remarks;

}