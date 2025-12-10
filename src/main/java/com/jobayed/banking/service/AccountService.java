package com.jobayed.banking.service;

import com.jobayed.banking.entity.Account;
import com.jobayed.banking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;

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

    public Page<Account> searchAccounts(String query, Pageable pageable) {
        log.info("Searching accounts with query: {}", query);
        return accountRepository.searchAccounts(query, pageable);
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

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    log.error("Error fetching currency rates", e);
                    return Mono.just("{\"error\": \"Unable to fetch rates\"}");
                })
                .block(); // Blocking for simplicity in this synchronous service, usually would return
                          // Mono/Flux
    }
}
