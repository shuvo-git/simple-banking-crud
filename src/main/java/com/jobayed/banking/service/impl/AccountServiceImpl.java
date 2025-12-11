package com.jobayed.banking.service.impl;

import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.mapper.AccountMapper;
import com.jobayed.banking.repository.AccountRepository;
import com.jobayed.banking.repository.specification.AccountSpecification;
import com.jobayed.banking.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RestClient.Builder restClientBuilder;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, RestClient.Builder restClientBuilder,
            AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.restClientBuilder = restClientBuilder;
        this.accountMapper = accountMapper;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        log.info("Creating account for: {} {}", request.getFirstName(), request.getLastName());

        // Generate Account Number
        String accountNumber;
        do {
            accountNumber = generateUniqueAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .socialSecurityNumber(request.getSocialSecurityNumber())
                .accountType(request.getAccountType())
                .balance(request.getBalance() != null ? request.getBalance() : 0.0)
                .currency(request.getCurrency())
                .branch(request.getBranch())
                .address(request.getAddress())
                .accountNumber(accountNumber)
                .accountStatus(com.jobayed.banking.enums.AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    private String generateUniqueAccountNumber() {
        String bankCode = "10"; // Default Bank Code
        String branchCode = "20"; // Default Branch Code
        // Use last 5 digits of timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String timeSegment = timestamp.substring(timestamp.length() - 5);

        Random random = new Random();

        while (true) {
            // Generate 4 digit random number
            int rand = 1000 + random.nextInt(9000);
            String accountNumber = bankCode + branchCode + timeSegment + rand;

            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountNumber, boolean withLedger) {
        Account account;
        if (withLedger) {
            account = accountRepository.findByAccountNumberWithLedgers(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
        } else {
            account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
        }
        return accountMapper.toResponse(account);
    }

    public Page<AccountResponse> searchAccounts(SearchRequest request, Pageable pageable) {
        log.info("Searching accounts with request: {}", request);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        LocalDate startDate;
        LocalDate endDate;

        // Resolve End Date
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            endDate = LocalDate.parse(request.getEndDate(), formatter);
        } else {
            endDate = LocalDate.now();
        }
        request.setEnd(endDate);

        // Resolve Start Date
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            startDate = LocalDate.parse(request.getStartDate(), formatter);
        } else {
            startDate = endDate.minusMonths(1);
        }
        request.setStart(startDate);

        log.info("Resolved Search Date Range: Start={}, End={}", request.getStart(), request.getEnd());

        Specification<Account> spec = AccountSpecification.from(request);
        return accountRepository.findAll(spec, pageable).map(accountMapper::toResponse);
    }

    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(accountMapper::toResponse);
    }

    @Transactional
    public AccountResponse updateAccount(String accountNumber, Account accountDetails) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));

        account.setFirstName(accountDetails.getFirstName());
        account.setLastName(accountDetails.getLastName());
        account.setEmail(accountDetails.getEmail());
        account.setPhone(accountDetails.getPhone());
        account.setAddress(accountDetails.getAddress());
        account.setBranch(accountDetails.getBranch());
        account.setAccountType(accountDetails.getAccountType());
        account.setAccountStatus(accountDetails.getAccountStatus());
        account.setCurrency(accountDetails.getCurrency());
        account.setBalance(accountDetails.getBalance());

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
        accountRepository.delete(account);
    }

    @Override
    public String getCurrencyRates() {
        // Example external API: ExchangeRate-API (Free tier)
        // Using a sample URL or a mock service for demonstration
        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        try {
            return restClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("Error fetching currency rates", e);
            return "{\"error\": \"Unable to fetch rates\"}";
        }
    }
}