package com.cryonex.customer.entity;

import com.cryonex.customer.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_address")
public class CustomerAddress extends BaseEntity {

    @Id
    @Column(name = "address_id", length = 20)
    private String addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 20)
    private AddressType addressType;

    @Column(name = "door_number", length = 20)
    private String doorNumber;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

}