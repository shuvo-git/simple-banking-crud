package com.jobayed.banking.service;

import com.jobayed.banking.controllers.dto.request.TransactionRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;

public interface TransactionService {
    AccountResponse performTransaction(String accountNumber, TransactionRequest request);
}
