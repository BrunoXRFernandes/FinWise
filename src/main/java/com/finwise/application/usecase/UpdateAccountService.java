package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.exception.ResourceNotFoundException;
import com.finwise.domain.model.Account;
import com.finwise.domain.port.in.UpdateAccountUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateAccountService implements UpdateAccountUseCase {
    private final AccountRepositoryPort accountRepository;

    public UpdateAccountService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account update(UUID accountId, UpdateAccountCommand command) {
        if (accountId == null) {
            throw new DomainValidationException("Account id is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new DomainValidationException("Account name is required");
        }

        Account current = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        Account updated = new Account(
                current.id(),
                command.name().trim(),
                current.type(),
                current.balance(),
                current.currency(),
                current.createdAt()
        );

        return accountRepository.save(updated);
    }
}

