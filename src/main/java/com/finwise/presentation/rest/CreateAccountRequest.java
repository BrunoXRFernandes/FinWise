package com.finwise.presentation.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(
        @NotNull UUID userId,
        @NotBlank String name,
        @NotBlank String type,
        @NotBlank String currency
) {
}

