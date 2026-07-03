package com.cryonex.customer.entity;

import com.cryonex.customer.enums.CommunicationMode;
import com.cryonex.customer.enums.Language;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_preferences")
public class CustomerPreference extends BaseEntity {

    @Id
    @Column(name = "preference_id", length = 20)
    private String preferenceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, length = 20)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_mode", nullable = false, length = 20)
    private CommunicationMode communicationMode;

    @Column(name = "email_enabled")
    private Boolean emailEnabled = true;

    @Column(name = "sms_enabled")
    private Boolean smsEnabled = true;

    @Column(name = "marketing_enabled")
    private Boolean marketingEnabled = false;

}