package com.jobayed.banking.controllers.dto.request;

import com.jobayed.banking.enums.AccountType;
import lombok.Data;

@Data
public class AccountRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String socialSecurityNumber;
    private AccountType accountType;
    private Double balance;
    private String currency;
    private String branch;
    private String address;
}
