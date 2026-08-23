package com.cryonex.account.entity;

import com.cryonex.account.enums.OperationMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "joint_account_holder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JointAccountHolder extends BaseEntity {

    @Id
    @Column(name = "holder_id", length = 20)
    private String holderId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "customer_id", length = 20, nullable = false)
    private String customerId;

    @Column(name = "holder_name", length = 100, nullable = false)
    private String holderName;

    @Column(name = "relationship", length = 50, nullable = false)
    private String relationship;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_mode", length = 30, nullable = false)
    private OperationMode operationMode;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

}