package com.finwise.presentation.rest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull UUID accountId,
        @NotNull UUID userId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank String category,
        String description,
        @NotNull LocalDate date,
        @NotBlank String type
) {
}

