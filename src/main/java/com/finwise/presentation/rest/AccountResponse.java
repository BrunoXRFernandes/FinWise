package com.finwise.presentation.rest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        String type,
        BigDecimal balance,
        String currency,
        Instant createdAt
) {
}

