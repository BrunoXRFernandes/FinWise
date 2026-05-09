package com.finwise.presentation.rest;

import jakarta.validation.constraints.NotBlank;

public record UpdateAccountRequest(
        @NotBlank String name
) {
}

