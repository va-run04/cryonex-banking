package com.cryonex.account.entity;

import com.cryonex.account.enums.FreezeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_freeze")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountFreeze extends BaseEntity {

    @Id
    @Column(name = "freeze_id", length = 20)
    private String freezeId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "freeze_type", length = 20, nullable = false)
    private FreezeType freezeType;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

}