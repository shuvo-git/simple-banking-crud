package com.jobayed.banking.controllers.dto.request;

import com.jobayed.banking.enums.AccountStatus;
import com.jobayed.banking.enums.AccountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String socialSecurityNumber;

    private AccountType accountType; // SAVINGS, CURRENT

    private Double balance;

    private String currency;
    private String branch;
    private AccountStatus accountStatus; // ACTIVE, INACTIVE, DELETED

    private LocalDate openedOn;
    private LocalDate closedOn;

    private String address;
}
