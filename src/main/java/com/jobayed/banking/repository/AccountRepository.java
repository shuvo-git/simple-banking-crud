package com.jobayed.banking.repository;

import com.jobayed.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.ledgers WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithLedgers(@Param("accountNumber") String accountNumber);
}
