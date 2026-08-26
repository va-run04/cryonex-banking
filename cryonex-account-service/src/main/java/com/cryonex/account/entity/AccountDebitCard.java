package com.cryonex.account.entity;

import com.cryonex.account.enums.CardType;
import com.cryonex.account.enums.CardVariant;
import com.cryonex.account.enums.DebitCardStatus;
import com.cryonex.account.enums.DeliveryMode;
import com.cryonex.account.enums.IssueType;
import com.cryonex.account.enums.RequestMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "account_debit_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDebitCard extends BaseEntity {

    @Id
    @Column(name = "debit_card_id", length = 20)
    private String debitCardId;

    @Column(name = "account_id", length = 20, nullable = false)
    private String accountId;

    @Column(name = "customer_id", length = 20, nullable = false)
    private String customerId;

    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", length = 20, nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_variant", length = 20, nullable = false)
    private CardVariant cardVariant;

    @Column(name = "card_holder_name", length = 100, nullable = false)
    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", length = 20, nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_mode", length = 30, nullable = false)
    private RequestMode requestMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 30, nullable = false)
    private DeliveryMode deliveryMode;

    @Column(name = "dispatch_address", length = 500)
    private String dispatchAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", length = 20, nullable = false)
    private DebitCardStatus cardStatus;

    @Column(name = "expiry_month")
    private Integer expiryMonth;

    @Column(name = "expiry_year")
    private Integer expiryYear;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "dispatched_date")
    private LocalDate dispatchedDate;

    @Column(name = "activated_date")
    private LocalDate activatedDate;

    @Column(name = "remarks", length = 500)
    private String remarks;

}