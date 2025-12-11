package com.jobayed.banking.controllers.endpoint;

import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.controllers.dto.request.TransactionRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.entity.Account;
import com.jobayed.banking.service.AccountService;
import com.jobayed.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Accounts", description = "Endpoints for managing accounts and transactions")
public class AccountResource {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping("/{accountNumber}/transactions")
    @Operation(summary = "Initiate a transaction", description = "Perform a credit or debit transaction on an account")
    public ResponseEntity<AccountResponse> initiateTransaction(
            @Parameter(description = "Account Number") @PathVariable String accountNumber,
            @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.performTransaction(accountNumber, request));
    }

    @GetMapping("/search")
    @Operation(summary = "Search accounts", description = "Search accounts with filters and pagination")
    public ResponseEntity<Page<AccountResponse>> searchAccounts(
            @ModelAttribute SearchRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.searchAccounts(request, pageable));
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Get a paginated list of all accounts")
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(accountService.getAllAccounts(pageable));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account details", description = "Retrieve account details by account number")
    public ResponseEntity<AccountResponse> getAccount(
            @Parameter(description = "Account Number") @PathVariable String accountNumber,
            @Parameter(description = "Include transaction history") @RequestParam(defaultValue = "false") boolean withLedger) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber, withLedger));
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new bank account")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @PutMapping("/{accountNumber}")
    @Operation(summary = "Update account", description = "Update existing account details")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account Number") @PathVariable String accountNumber,
            @RequestBody Account account) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, account));
    }

    @DeleteMapping("/{accountNumber}")
    @Operation(summary = "Delete account", description = "Delete an account by account number")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account Number") @PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/currency-rates")
    @Operation(summary = "Get currency rates", description = "Fetch current currency exchange rates")
    public ResponseEntity<String> getCurrencyRates() {
        return ResponseEntity.ok(accountService.getCurrencyRates());
    }
}
