package com.cryonex.account.entity;

import com.cryonex.account.enums.AccountTypeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_code", length = 20, nullable = false, unique = true)
    private String typeCode;

    @Column(name = "type_name", length = 50, nullable = false, unique = true)
    private String typeName;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "minimum_balance", precision = 18, scale = 2)
    private BigDecimal minimumBalance;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "withdrawal_limit", precision = 18, scale = 2)
    private BigDecimal withdrawalLimit;

    @Column(name = "daily_transfer_limit", precision = 18, scale = 2)
    private BigDecimal dailyTransferLimit;

    @Column(name = "maintenance_charge", precision = 18, scale = 2)
    private BigDecimal maintenanceCharge;

    @Column(name = "cheque_book_available", length = 1)
    private String chequeBookAvailable;

    @Column(name = "debit_card_available", length = 1)
    private String debitCardAvailable;

    @Column(name = "passbook_available", length = 1)
    private String passbookAvailable;

    @Column(name = "overdraft_allowed", length = 1)
    private String overdraftAllowed;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)
    private AccountTypeStatus accountStatus;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

}