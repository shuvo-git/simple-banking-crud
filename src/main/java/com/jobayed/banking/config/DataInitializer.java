package com.jobayed.banking.config;

import com.jobayed.banking.entity.Account;
import com.jobayed.banking.entity.Ledger;
import com.jobayed.banking.enums.AccountStatus;
import com.jobayed.banking.enums.AccountType;
import com.jobayed.banking.enums.TransactionType;
import com.jobayed.banking.repository.AccountRepository;
import com.jobayed.banking.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            log.info("Seeding initial data...");

            // Create Account 1
            Account account1 = Account.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .phone("+1-555-0101")
                    .socialSecurityNumber("XXX-XX-1234")
                    .accountNumber("102023001")
                    .accountType(AccountType.SAVINGS)
                    .balance(5000.0)
                    .currency("USD")
                    .branch("Downtown")
                    .address("123 Main St, New York, NY")
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            account1 = accountRepository.save(account1);

            // Create Ledger for Account 1
            createLedger(account1, 1000.0, TransactionType.CREDIT, "Initial Deposit");
            createLedger(account1, 200.0, TransactionType.DEBIT, "ATM Withdrawal");

            // Create Account 2
            Account account2 = Account.builder()
                    .firstName("Jane")
                    .lastName("Smith")
                    .email("jane.smith@example.com")
                    .phone("+1-555-0102")
                    .socialSecurityNumber("XXX-XX-5678")
                    .accountNumber("102023002")
                    .accountType(AccountType.SAVINGS)
                    .balance(12500.50)
                    .currency("USD")
                    .branch("Uptown")
                    .address("456 Market Ave, San Francisco, CA")
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            account2 = accountRepository.save(account2);

            // Create Ledger for Account 2
            createLedger(account2, 12000.0, TransactionType.CREDIT, "Salary Deposit");
            createLedger(account2, 50.0, TransactionType.DEBIT, "Coffee Shop");

            log.info("Data seeding completed.");
        }
    }

    private void createLedger(Account account, Double amount, TransactionType type, String description) {
        Ledger ledger = Ledger.builder()
                .transactionId(UUID.randomUUID())
                .account(account)
                .amount(amount)
                .transactionType(type)
                .transactionDate(LocalDateTime.now())
                .balanceAfter(account.getBalance()) // Note: simple logic for demo
                .description(description)
                .build();
        ledgerRepository.save(ledger);
    }
}
