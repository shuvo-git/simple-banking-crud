package com.jobayed.banking.entity;

import com.jobayed.banking.enums.AccountStatus;
import com.jobayed.banking.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseAuditingEntity {
    @Column(name = "fname", nullable = false)
    private String firstName;

    @Column(name = "lname", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "ssn", nullable = false)
    private String socialSecurityNumber;

    @Column(name = "ac", nullable = false)
    private String accountNumber;

    @Column(name = "ac_typ", nullable = false)
    private AccountType accountType; // SAVINGS, CURRENT

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "branch", nullable = false)
    private String branch;

    @Column(name = "sts", nullable = false)
    private AccountStatus accountStatus; // ACTIVE, INACTIVE, DELETED

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "account", fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<Ledger> ledgers;
}
