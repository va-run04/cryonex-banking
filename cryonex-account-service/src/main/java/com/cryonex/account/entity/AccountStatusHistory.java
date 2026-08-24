package com.cryonex.account.entity;

import com.cryonex.account.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusHistory extends BaseEntity {

    @Id
    @Column(name = "history_id", length = 20)
    private String historyId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 20, nullable = false)
    private AccountStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", length = 20, nullable = false)
    private AccountStatus newStatus;

    @Column(name = "changed_by", length = 50, nullable = false)
    private String changedBy;

    @Column(name = "reason", length = 500)
    private String reason;

}