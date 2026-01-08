package com.jobayed.banking.mapper;

import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import org.springframework.stereotype.Component;

import static jakarta.persistence.Persistence.getPersistenceUtil;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        AccountResponse response = new AccountResponse();
        response.setFirstName(account.getFirstName());
        response.setLastName(account.getLastName());
        response.setEmail(account.getEmail());
        response.setPhone(account.getPhone());
        response.setSocialSecurityNumber(account.getSocialSecurityNumber());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setBranch(account.getBranch());
        response.setAccountStatus(account.getAccountStatus());
        response.setAddress(account.getAddress());

        if (account.getLedgers() != null
                && getPersistenceUtil().isLoaded(account.getLedgers())) {
            response.setTransactions(account.getLedgers().stream()
                    .map(ledger -> com.jobayed.banking.controllers.dto.response.LedgerResponse.builder()
                            .transactionId(ledger.getTransactionId())
                            .amount(ledger.getAmount())
                            .transactionType(ledger.getTransactionType())
                            .transactionDate(ledger.getTransactionDate())
                            .balanceAfter(ledger.getBalanceAfter())
                            .description(ledger.getDescription())
                            .build())
                    .collect(java.util.stream.Collectors.toList()));
        }

        return response;
    }
}
