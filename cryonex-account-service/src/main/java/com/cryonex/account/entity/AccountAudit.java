package com.cryonex.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAudit extends BaseEntity {

    @Id
    @Column(name = "audit_id", length = 20)
    private String auditId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "action", length = 50, nullable = false)
    private String action;

    @Column(name = "performed_by", length = 50, nullable = false)
    private String performedBy;

}