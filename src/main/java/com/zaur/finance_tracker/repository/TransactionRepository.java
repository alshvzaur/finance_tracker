package com.zaur.finance_tracker.repository;

import com.zaur.finance_tracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUserId(UUID userId);

    @Query("""
    SELECT t.currency, t.type, SUM(t.amount)
    FROM Transaction t
    WHERE t.userId = :userId
    GROUP BY t.currency, t.type
""")
    List<Object[]> getTransactionSummaryByUserId(@Param("userId") UUID userId);
}
