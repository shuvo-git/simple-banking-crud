package com.jobayed.banking.repository;

import com.jobayed.banking.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Optional<Ledger> findByTransactionId(UUID transactionId);
}
