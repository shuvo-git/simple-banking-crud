package com.jobayed.banking.controllers.dto.request;

import com.jobayed.banking.enums.AccountType;
import lombok.Data;

@Data
public class SearchRequest {
    private String query;
    private AccountType accountType;
    private Double minBalance;
    private Double maxBalance;
    private String branch;
}
