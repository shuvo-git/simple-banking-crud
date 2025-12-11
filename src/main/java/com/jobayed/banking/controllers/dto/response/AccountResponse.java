package com.jobayed.banking.controllers.dto.response;

import com.jobayed.banking.enums.AccountStatus;
import com.jobayed.banking.enums.AccountType;
import lombok.Data;

@Data
public class AccountResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String socialSecurityNumber;

    private String accountNumber;
    private AccountType accountType; // SAVINGS, CURRENT

    private Double balance;

    private String currency;
    private String branch;
    private AccountStatus accountStatus; // ACTIVE, INACTIVE, DELETED

    private String address;
    private java.util.List<LedgerResponse> transactions;
}
