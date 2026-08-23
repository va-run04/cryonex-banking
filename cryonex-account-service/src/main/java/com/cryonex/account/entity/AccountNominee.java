package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account_nominee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountNominee extends BaseEntity {

    @Id
    @Column(name = "nominee_id", length = 20)
    private String nomineeId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "nominee_name", length = 100, nullable = false)
    private String nomineeName;

    @Column(name = "relationship", length = 50, nullable = false)
    private String relationship;

    @Column(name = "mobile_number", length = 15, nullable = false)
    private String mobileNumber;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "share_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal sharePercentage;

    @Column(name = "verification_status", length = 20, nullable = false)
    private String verificationStatus;

}