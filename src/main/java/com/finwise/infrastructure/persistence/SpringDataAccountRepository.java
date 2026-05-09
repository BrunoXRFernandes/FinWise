package com.finwise.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountJpaEntity, UUID> {
    @Query(value = """
            SELECT a.*
            FROM accounts a
            JOIN user_accounts ua ON ua.account_id = a.id
            WHERE ua.user_id = :userId
            ORDER BY a.created_at DESC
            """, nativeQuery = true)
    List<AccountJpaEntity> findAllByUserId(@Param("userId") UUID userId);
}

