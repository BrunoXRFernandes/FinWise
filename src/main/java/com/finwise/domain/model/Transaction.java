package com.finwise.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Transaction(
        UUID id,
        UUID accountId,
        UUID userId,
        BigDecimal amount,
        String category,
        String description,
        LocalDate date,
        TransactionType type
) {
}

