package com.finwise.infrastructure.persistence;

import com.finwise.domain.model.Transaction;
import com.finwise.domain.port.out.TransactionRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class JpaTransactionRepositoryAdapter implements TransactionRepositoryPort {
    private final SpringDataTransactionRepository transactionRepository;
    private final TransactionPersistenceMapper mapper;

    public JpaTransactionRepositoryAdapter(
            SpringDataTransactionRepository transactionRepository,
            TransactionPersistenceMapper mapper
    ) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity saved = transactionRepository.save(mapper.toEntity(transaction));
        return mapper.toDomain(saved);
    }

    @Override
    public List<Transaction> findByFilter(UUID accountId, LocalDate fromDate, LocalDate toDate, String type) {
        return transactionRepository.findByFilter(accountId, fromDate, toDate, type)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

