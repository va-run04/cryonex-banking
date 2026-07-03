package com.cryonex.customer.entity;

import com.cryonex.customer.enums.ContactMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer_contact")
public class CustomerContact extends BaseEntity {

    @Id
    @Column(name = "contact_id", length = 20)
    private String contactId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(name = "alternate_mobile", length = 10)
    private String alternateMobile;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "landline", length = 15)
    private String landline;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_contact_mode", length = 10)
    private ContactMode preferredContactMode;

}