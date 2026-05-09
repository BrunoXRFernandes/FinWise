package com.finwise.infrastructure.persistence;

import com.finwise.domain.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionPersistenceMapper {
    public TransactionJpaEntity toEntity(Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.id(),
                transaction.accountId(),
                transaction.userId(),
                transaction.amount(),
                transaction.category(),
                transaction.description(),
                transaction.date(),
                transaction.type()
        );
    }

    public Transaction toDomain(TransactionJpaEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getUserId(),
                entity.getAmount(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getDate(),
                entity.getType()
        );
    }
}

