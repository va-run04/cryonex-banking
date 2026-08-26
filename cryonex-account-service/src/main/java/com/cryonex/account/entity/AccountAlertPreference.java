package com.cryonex.account.entity;

import com.cryonex.account.enums.NotificationMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_alert_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAlertPreference extends BaseEntity {

    @Id
    @Column(name = "preference_id", length = 20)
    private String preferenceId;

    @Column(name = "account_id", length = 20, nullable = false, unique = true)
    private String accountId;

    @Column(name = "debit_alert", length = 1)
    private String debitAlert;

    @Column(name = "credit_alert", length = 1)
    private String creditAlert;

    @Column(name = "low_balance_alert", length = 1)
    private String lowBalanceAlert;

    @Column(name = "minimum_balance", precision = 18, scale = 2)
    private BigDecimal minimumBalance;

    @Column(name = "cheque_bounce_alert", length = 1)
    private String chequeBounceAlert;

    @Column(name = "emi_due_alert", length = 1)
    private String emiDueAlert;

    @Column(name = "interest_credit_alert", length = 1)
    private String interestCreditAlert;

    @Column(name = "login_alert", length = 1)
    private String loginAlert;

    @Column(name = "large_transaction_alert", length = 1)
    private String largeTransactionAlert;

    @Column(name = "large_transaction_amount", precision = 18, scale = 2)
    private BigDecimal largeTransactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_mode", length = 20, nullable = false)
    private NotificationMode notificationMode;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

}