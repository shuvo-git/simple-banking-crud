package com.jobayed.banking.service.impl;

import com.jobayed.banking.controllers.dto.request.TransactionRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.entity.Ledger;
import com.jobayed.banking.enums.TransactionType;
import com.jobayed.banking.mapper.AccountMapper;
import com.jobayed.banking.repository.AccountRepository;
import com.jobayed.banking.repository.LedgerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerRepository ledgerRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber("123456");
        account.setBalance(100.0);
    }

    @Test
    void performTransaction_Credit_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(50.0);
        request.setTransactionType(TransactionType.CREDIT);

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toResponse(any(Account.class))).thenReturn(new AccountResponse());

        transactionService.performTransaction("123456", request);

        // Balance 100 + 50 = 150
        assertEquals(150.0, account.getBalance());
        verify(ledgerRepository).save(any(Ledger.class));
    }

    @Test
    void performTransaction_Debit_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(50.0);
        request.setTransactionType(TransactionType.DEBIT);

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toResponse(any(Account.class))).thenReturn(new AccountResponse());

        transactionService.performTransaction("123456", request);

        // Balance 100 - 50 = 50
        assertEquals(50.0, account.getBalance());
        verify(ledgerRepository).save(any(Ledger.class));
    }

    @Test
    void performTransaction_Debit_InsufficientFunds() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(150.0);
        request.setTransactionType(TransactionType.DEBIT);

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.performTransaction("123456", request));

        assertEquals("Insufficient funds", exception.getMessage());
        assertEquals(100.0, account.getBalance()); // Balance unchanged
        verify(ledgerRepository, never()).save(any(Ledger.class));
    }
}
