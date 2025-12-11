package com.jobayed.banking.service.impl;

import com.jobayed.banking.controllers.dto.request.TransactionRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.entity.Ledger;
import com.jobayed.banking.enums.TransactionType;
import com.jobayed.banking.mapper.AccountMapper;
import com.jobayed.banking.repository.AccountRepository;
import com.jobayed.banking.repository.LedgerRepository;
import com.jobayed.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AccountResponse performTransaction(String accountNumber, TransactionRequest request) {
        log.info("Initiating transaction for account: {}, type: {}, amount: {}",
                accountNumber, request.getTransactionType(), request.getAmount());

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        if (TransactionType.DEBIT.equals(request.getTransactionType())) {
            if (account.getBalance() < request.getAmount()) {
                throw new RuntimeException("Insufficient funds");
            }
            account.setBalance(account.getBalance() - request.getAmount());
        } else if (TransactionType.CREDIT.equals(request.getTransactionType())) {
            account.setBalance(account.getBalance() + request.getAmount());
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        Account savedAccount = accountRepository.save(account);

        Ledger ledger = Ledger.builder()
                .transactionId(UUID.randomUUID())
                .account(savedAccount)
                .amount(request.getAmount())
                .transactionType(request.getTransactionType())
                .transactionDate(LocalDateTime.now())
                .balanceAfter(savedAccount.getBalance())
                .description(request.getDescription())
                .build();

        ledgerRepository.save(ledger);

        log.info("Transaction completed successfully. New Balance: {}", savedAccount.getBalance());

        return accountMapper.toResponse(savedAccount);
    }
}
