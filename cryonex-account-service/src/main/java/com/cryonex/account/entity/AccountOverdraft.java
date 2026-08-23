package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_overdraft")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountOverdraft extends BaseEntity {

    @Id
    @Column(name = "overdraft_id", length = 20)
    private String overdraftId;

    @Column(name = "account_id", length = 20, nullable = false, unique = true)
    private String accountId;

    @Column(name = "overdraft_limit", precision = 18, scale = 2, nullable = false)
    private BigDecimal overdraftLimit;

    @Column(name = "interest_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

}