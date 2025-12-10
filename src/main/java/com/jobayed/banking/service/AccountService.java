package com.jobayed.banking.service;

import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AccountService {

    void create(AccountRequest request);

    void update(String accountNumber, AccountRequest request);

    void delete(String accountNumber);

    AccountResponse getAccountInfo(String accountNumber);

    Page<AccountResponse> search(SearchRequest searchRequest, PageRequest pageRequest);
}
