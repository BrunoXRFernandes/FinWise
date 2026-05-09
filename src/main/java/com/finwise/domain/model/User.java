package com.finwise.domain.model;

import java.time.Instant;
import java.util.UUID;

public record User(
        UUID id,
        String email,
        String passwordHash,
        String name,
        Instant createdAt
) {
}

