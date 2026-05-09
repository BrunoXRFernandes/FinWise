package com.finwise.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataTransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    @Query(value = """
            SELECT t.*
            FROM transactions t
            WHERE (:accountId IS NULL OR t.account_id = :accountId)
              AND (:fromDate IS NULL OR t.date >= :fromDate)
              AND (:toDate IS NULL OR t.date <= :toDate)
              AND (:type IS NULL OR t.type = :type)
            ORDER BY t.date DESC
            """, nativeQuery = true)
    List<TransactionJpaEntity> findByFilter(
            @Param("accountId") UUID accountId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("type") String type
    );
}

