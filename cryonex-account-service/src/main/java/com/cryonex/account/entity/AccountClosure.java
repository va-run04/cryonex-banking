package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_closure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountClosure extends BaseEntity {

    @Id
    @Column(name = "closure_id", length = 20)
    private String closureId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "closure_reason", length = 500, nullable = false)
    private String closureReason;

    @Column(name = "closure_status", length = 20, nullable = false)
    private String closureStatus;

    @Column(name = "cancelled_by", length = 50)
    private String cancelledBy;

    @Column(name = "cancelled_date")
    private java.time.LocalDateTime cancelledDate;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

}