package com.jobayed.banking.controllers.endpoint;

import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Vantage Labs LLC.
 * User: Jobayed Ullah
 * Time: 12/10/25 7:42 PM
 */

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountResource {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> searchAccounts(
            @ModelAttribute SearchRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.searchAccounts(request, pageable));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable String accountNumber,
            @RequestBody Account account) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, account));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountNumber}/with-transactions")
    public ResponseEntity<AccountResponse> getAccountWithTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountWithTransactions(accountNumber));
    }

    @GetMapping("/currency-rates")
    public ResponseEntity<String> getCurrencyRates() {
        return ResponseEntity.ok(accountService.getCurrencyRates());
    }
}
