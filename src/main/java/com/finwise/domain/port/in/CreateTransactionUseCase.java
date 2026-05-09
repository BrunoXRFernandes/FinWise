package com.finwise.domain.port.in;

import com.finwise.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CreateTransactionUseCase {
    Transaction create(CreateTransactionCommand command);

    record CreateTransactionCommand(
            UUID accountId,
            UUID userId,
            BigDecimal amount,
            String category,
            String description,
            LocalDate date,
            String type
    ) {
    }
}

