package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.model.Transaction;
import com.finwise.domain.model.TransactionType;
import com.finwise.domain.port.in.ListTransactionsUseCase;
import com.finwise.domain.port.out.TransactionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class ListTransactionsService implements ListTransactionsUseCase {
    private final TransactionRepositoryPort transactionRepository;

    public ListTransactionsService(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> list(TransactionFilter filter) {
        validateFilter(filter);

        String normalizedType = normalizeType(filter.type());
        return transactionRepository.findByFilter(
                filter.accountId(),
                filter.fromDate(),
                filter.toDate(),
                normalizedType
        );
    }

    private void validateFilter(TransactionFilter filter) {
        LocalDate fromDate = filter.fromDate();
        LocalDate toDate = filter.toDate();
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new DomainValidationException("fromDate must be before or equal to toDate");
        }
    }

    private String normalizeType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }

        String normalized = type.trim().toUpperCase(Locale.ROOT);
        try {
            TransactionType.valueOf(normalized);
            return normalized;
        } catch (IllegalArgumentException ex) {
            throw new DomainValidationException("Invalid transaction type filter: " + type);
        }
    }
}

