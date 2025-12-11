package com.jobayed.banking.service.impl;

import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.enums.AccountType;
import com.jobayed.banking.mapper.AccountMapper;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

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

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

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
        AccountRequest request = new AccountRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");

        AccountResponse response = new AccountResponse();
        response.setAccountNumber("1020123456789");

        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(response);

        AccountResponse created = accountService.createAccount(request);

        assertNotNull(created);
        assertEquals("1020123456789", created.getAccountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getAccount_Success() {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(response);

        AccountResponse found = accountService.getAccount("12345", false);

        assertNotNull(found);
        assertEquals("12345", found.getAccountNumber());
    }

    @Test
    void getAccount_WithLedger_Success() {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber("12345");
        // Simplified verification; deep inspection of ledger list mapping would ideally
        // happen in Mapper test

        when(accountRepository.findByAccountNumberWithLedgers("12345")).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(response);

        AccountResponse found = accountService.getAccount("12345", true);

        assertNotNull(found);
        verify(accountRepository, times(1)).findByAccountNumberWithLedgers("12345");
    }

    @Test
    void getAccount_NotFound() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.getAccount("12345", false));
    }

    @Test
    void searchAccounts_Success() {
        Page<Account> page = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        AccountResponse response = new AccountResponse();
        when(accountMapper.toResponse(any(Account.class))).thenReturn(response);

        SearchRequest request = new SearchRequest();
        request.setQuery("John");

        Page<AccountResponse> result = accountService.searchAccounts(request, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        // Verify default dates are set in hidden fields
        assertNotNull(request.getEnd());
        assertNotNull(request.getStart());
    }

    @Test
    void updateAccount_Success() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse response = new AccountResponse();
        response.setFirstName("Jane");
        when(accountMapper.toResponse(any(Account.class))).thenReturn(response);

        Account updateDetails = new Account();
        updateDetails.setFirstName("Jane");
        updateDetails.setLastName("Doe");
        // ... set other fields

        AccountResponse updated = accountService.updateAccount("12345", updateDetails);

        assertNotNull(updated);
        assertEquals("Jane", updated.getFirstName());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void deleteAccount_Success() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        accountService.deleteAccount("12345");

        verify(accountRepository, times(1)).delete(account);
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
