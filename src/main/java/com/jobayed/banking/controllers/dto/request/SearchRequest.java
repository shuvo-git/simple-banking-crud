package com.jobayed.banking.controllers.dto.request;

import com.jobayed.banking.enums.AccountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchRequest {
    private String query;
    private AccountType accountType;
    private Double minBalance;
    private Double maxBalance;
    private String branch;
    private String startDate;
    private String endDate;

    // Internal fields for JPA Specification
    private LocalDate start;
    private LocalDate end;
}
