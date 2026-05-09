package com.finwise.domain.port.in;

import com.finwise.domain.model.Account;

import java.util.UUID;

public interface CreateAccountUseCase {
    Account create(CreateAccountCommand command);

    record CreateAccountCommand(UUID userId, String name, String type, String currency) {
    }
}

