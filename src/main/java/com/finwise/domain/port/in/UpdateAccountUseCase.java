package com.finwise.domain.port.in;

import com.finwise.domain.model.Account;

import java.util.UUID;

public interface UpdateAccountUseCase {
    Account update(UUID accountId, UpdateAccountCommand command);

    record UpdateAccountCommand(String name) {
    }
}

