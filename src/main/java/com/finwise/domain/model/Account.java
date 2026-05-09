package com.finwise.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Account(
        UUID id,
        String name,
        AccountType type,
        BigDecimal balance,
        String currency,
        Instant createdAt
) {
}

