package com.jobayed.banking.controllers.dto.request;

import com.jobayed.banking.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private Double amount;
    private TransactionType transactionType;
    private String description;
    // We assume the target account is identified by the path variable or this
    // request is for the 'self' account operation.
    // If transfers between accounts are needed, we might add 'targetAccountNumber'.
    // For now, simple Debit/Credit on the account specified in the URL.
}
