package com.jobayed.banking.entity;

import com.jobayed.banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ledger")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ledger extends BaseAuditingEntity {

    @Column(nullable = false, unique = true)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private java.time.LocalDateTime transactionDate;

    // Snapshot of the account balance after this transaction was applied
    @Column(nullable = false)
    private Double balanceAfter;

    // Optional: Reference to describe the transaction source (e.g. "ATM",
    // "Payment", "Transfer")
    private String description;
}
