package com.jobayed.banking.service;

import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;

import com.jobayed.banking.repository.specification.AccountSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.jobayed.banking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final RestClient.Builder restClientBuilder;
    private final com.jobayed.banking.mapper.AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(Account account) {
        log.info("Creating account: {}", account);

        account.setAccountNumber(generateUniqueAccountNumber());

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    private String generateUniqueAccountNumber() {
        String bankCode = "10"; // Default Bank Code
        String branchCode = "20"; // Default Branch Code
        // Use last 5 digits of timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String timeSegment = timestamp.substring(timestamp.length() - 5);

        java.util.Random random = new java.util.Random();

        while (true) {
            // Generate 4 digit random number
            int rand = 1000 + random.nextInt(9000);
            String accountNumber = bankCode + branchCode + timeSegment + rand;

            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

    public AccountResponse getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
    }

    public Page<AccountResponse> searchAccounts(SearchRequest request, Pageable pageable) {
        log.info("Searching accounts with request: {}", request);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd");
        java.time.LocalDate endDate;
        java.time.LocalDate startDate;

        // Resolve End Date
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            endDate = java.time.LocalDate.parse(request.getEndDate(), formatter);
        } else {
            endDate = java.time.LocalDate.now();
            request.setEndDate(endDate.format(formatter));
        }

        // Resolve Start Date
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            startDate = java.time.LocalDate.parse(request.getStartDate(), formatter);
        } else {
            startDate = endDate.minusMonths(1);
            request.setStartDate(startDate.format(formatter));
        }

        log.info("Resolved Search Date Range: Start={}, End={}", request.getStartDate(), request.getEndDate());

        Specification<Account> spec = AccountSpecification.from(request);
        return accountRepository.findAll(spec, pageable).map(accountMapper::toResponse);
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

    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
        accountRepository.delete(account);
    }

    public AccountResponse getAccountWithTransactions(String accountNumber) {
        return accountRepository.findByAccountNumberWithTransactions(accountNumber)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
    }

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
