package com.jobayed.banking.service;

import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccount(String accountNumber, boolean withLedger);

    AccountResponse updateAccount(String accountNumber, Account account);

    void deleteAccount(String accountNumber);

    Page<AccountResponse> searchAccounts(SearchRequest request, Pageable pageable);

    Page<AccountResponse> getAllAccounts(Pageable pageable);

    String getCurrencyRates();
}
