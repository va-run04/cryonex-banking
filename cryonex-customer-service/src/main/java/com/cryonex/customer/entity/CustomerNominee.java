package com.cryonex.customer.entity;

import com.cryonex.customer.enums.Relationship;
import com.cryonex.customer.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_nominee")
public class CustomerNominee extends BaseEntity {

    @Id
    @Column(name = "nominee_id", length = 20)
    private String nomineeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "nominee_name", nullable = false, length = 100)
    private String nomineeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false, length = 20)
    private Relationship relationship;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    private VerificationStatus verificationStatus;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "share_percentage", precision = 5, scale = 2)
    private BigDecimal sharePercentage;

}