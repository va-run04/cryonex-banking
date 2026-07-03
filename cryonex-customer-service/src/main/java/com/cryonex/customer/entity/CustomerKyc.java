package com.cryonex.customer.entity;

import com.cryonex.customer.enums.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_kyc")
public class CustomerKyc extends BaseEntity {

    @Id
    @Column(name = "kyc_id", length = 20)
    private String kycId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Column(name = "pan_verified")
    private Boolean panVerified = false;

    @Column(name = "aadhaar_verified")
    private Boolean aadhaarVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus;

    @Column(name = "verified_by", length = 20)
    private String verifiedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

}