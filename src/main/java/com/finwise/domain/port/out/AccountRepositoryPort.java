package com.finwise.domain.port.out;

import com.finwise.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);

    void linkUserToAccount(UUID userId, UUID accountId);

    Optional<Account> findById(UUID id);

    List<Account> findAllByUserId(UUID userId);
}

