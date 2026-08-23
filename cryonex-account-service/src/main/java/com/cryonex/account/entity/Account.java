package com.cryonex.account.entity;

import com.cryonex.account.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    @Column(name = "account_id", length = 20)
    private String accountId;

    @Column(name = "customer_id", length = 20, nullable = false)
    private String customerId;

    @Column(name = "account_number", length = 20, unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", length = 20, nullable = false)
    private String accountType;

    @Column(name = "branch_code", length = 20, nullable = false)
    private String branchCode;

    @Column(name = "ifsc_code", length = 15, nullable = false)
    private String ifscCode;

    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @Column(name = "opening_balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal openingBalance;

    @Column(name = "available_balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "ledger_balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal ledgerBalance;

    @Column(name = "minimum_balance", precision = 18, scale = 2)
    private BigDecimal minimumBalance;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", length = 20, nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "opened_date", nullable = false)
    private LocalDateTime openedDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}