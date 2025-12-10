package com.jobayed.banking.repository;

import com.jobayed.banking.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
