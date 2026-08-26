package com.cryonex.account.entity;

import com.cryonex.account.enums.StatementFrequency;
import com.cryonex.account.enums.StatementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "account_statement_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementPreference extends BaseEntity {

    @Id
    @Column(name = "preference_id", length = 20)
    private String preferenceId;

    @Column(name = "account_id", length = 20, nullable = false, unique = true)
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "statement_type", length = 20, nullable = false)
    private StatementType statementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "statement_frequency", length = 20, nullable = false)
    private StatementFrequency statementFrequency;

    @Column(name = "email_id", length = 100)
    private String emailId;

    @Column(name = "password_protected", length = 1)
    private String passwordProtected;

    @Column(name = "delivery_status", length = 20, nullable = false)
    private String deliveryStatus;

    @Column(name = "last_generated_date")
    private LocalDate lastGeneratedDate;

}