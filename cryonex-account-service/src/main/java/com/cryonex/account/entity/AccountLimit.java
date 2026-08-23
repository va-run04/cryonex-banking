package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_limit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLimit extends BaseEntity {

    @Id
    @Column(name = "limit_id", length = 20)
    private String limitId;

    @Column(name = "account_id", length = 20, nullable = false, unique = true)
    private String accountId;

    @Column(name = "daily_atm_limit", precision = 18, scale = 2)
    private BigDecimal dailyAtmLimit;

    @Column(name = "daily_upi_limit", precision = 18, scale = 2)
    private BigDecimal dailyUpiLimit;

    @Column(name = "daily_neft_limit", precision = 18, scale = 2)
    private BigDecimal dailyNeftLimit;

    @Column(name = "daily_rtgs_limit", precision = 18, scale = 2)
    private BigDecimal dailyRtgsLimit;

    @Column(name = "daily_imps_limit", precision = 18, scale = 2)
    private BigDecimal dailyImpsLimit;

    @Column(name = "monthly_transfer_limit", precision = 18, scale = 2)
    private BigDecimal monthlyTransferLimit;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

}