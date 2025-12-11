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
    private final com.jobayed.banking.service.TransactionService transactionService;

    @PostMapping("/{accountNumber}/transactions")
    public ResponseEntity<AccountResponse> initiateTransaction(
            @PathVariable String accountNumber,
            @RequestBody com.jobayed.banking.controllers.dto.request.TransactionRequest request) {
        return ResponseEntity.ok(transactionService.performTransaction(accountNumber, request));
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> searchAccounts(
            @ModelAttribute SearchRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.searchAccounts(request, pageable));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber,
            @RequestParam(defaultValue = "false") boolean withLedger) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber, withLedger));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody com.jobayed.banking.controllers.dto.request.AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
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

    @GetMapping("/currency-rates")
    public ResponseEntity<String> getCurrencyRates() {
        return ResponseEntity.ok(accountService.getCurrencyRates());
    }
}
