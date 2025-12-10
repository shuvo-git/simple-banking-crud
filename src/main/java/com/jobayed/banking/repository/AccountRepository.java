package com.jobayed.banking.repository;

import com.jobayed.banking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "a.accountNumber LIKE CONCAT('%', :query, '%'))")
    Page<Account> searchAccounts(@Param("query") String query, Pageable pageable);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.transactions WHERE a.id = :id")
    Optional<Account> findByIdWithTransactions(@Param("id") Long id);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
