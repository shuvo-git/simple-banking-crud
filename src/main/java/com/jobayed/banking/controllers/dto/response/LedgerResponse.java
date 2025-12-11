package com.jobayed.banking.controllers.dto.response;

import com.jobayed.banking.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LedgerResponse {
    private UUID transactionId;
    private Double amount;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private Double balanceAfter;
    private String description;
}
