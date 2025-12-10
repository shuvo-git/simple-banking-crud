package com.jobayed.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog extends BaseAuditingEntity {
    private UUID transactionId;

    private Double amount;

    private String fromAccount;
    private String toAccount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
