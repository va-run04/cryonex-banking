package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "interest_configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestConfiguration extends BaseEntity {

    @Id
    @Column(name = "interest_id", length = 20)
    private String interestId;

    @Column(name = "account_type", length = 30, nullable = false)
    private String accountType;

    @Column(name = "interest_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}