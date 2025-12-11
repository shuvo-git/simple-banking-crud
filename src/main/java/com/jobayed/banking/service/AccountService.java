package com.jobayed.banking.service;

import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.entity.Account;
import jakarta.persistence.criteria.Predicate;
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

    @Transactional
    public Account createAccount(Account account) {
        log.info("Creating account: {}", account);
        // Basic validation/logic here if needed
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public Page<Account> searchAccounts(SearchRequest request, Pageable pageable) {
        log.info("Searching accounts with request: {}", request);
        Specification<Account> spec = AccountSpecification.from(request);
        return accountRepository.findAll(spec, pageable);
    }

    @Transactional
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = getAccount(id);
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

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = getAccount(id);
        accountRepository.delete(account);
    }

    public Account getAccountWithTransactions(Long id) {
        return accountRepository.findByIdWithTransactions(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
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
