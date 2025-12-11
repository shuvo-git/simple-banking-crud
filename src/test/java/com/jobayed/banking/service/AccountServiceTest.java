package com.jobayed.banking.service;

import com.jobayed.banking.entity.Account;
import com.jobayed.banking.enums.AccountType;
import com.jobayed.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setEmail("john.doe@example.com");
        account.setBalance(1000.0);
        account.setAccountType(AccountType.SAVINGS);
    }

    @Test
    void createAccount_Success() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account created = accountService.createAccount(account);

        assertNotNull(created);
        assertEquals("12345", created.getAccountNumber());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void getAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account found = accountService.getAccount(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void getAccount_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.getAccount(1L));
    }

    @Test
    void searchAccounts_Success() {
        Page<Account> page = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.searchAccounts(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Account> result = accountService.searchAccounts("John", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updateDetails = new Account();
        updateDetails.setFirstName("Jane");
        updateDetails.setLastName("Doe");
        // ... set other fields

        Account updated = accountService.updateAccount(1L, updateDetails);

        assertNotNull(updated);
        assertEquals("Jane", updated.getFirstName());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void deleteAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void getAccountWithTransactions_Success() {
        when(accountRepository.findByIdWithTransactions(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountWithTransactions(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCurrencyRates_Success() {
        String mockResponse = "{\"rates\": {\"EUR\": 0.85}}";

        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(mockResponse);

        String rates = accountService.getCurrencyRates();

        assertNotNull(rates);
        assertTrue(rates.contains("EUR"));
    }
}
