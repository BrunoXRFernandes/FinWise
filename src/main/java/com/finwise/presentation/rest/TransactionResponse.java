package com.finwise.presentation.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID userId,
        BigDecimal amount,
        String category,
        String description,
        LocalDate date,
        String type
) {
}

