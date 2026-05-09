package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.exception.ResourceNotFoundException;
import com.finwise.domain.model.Account;
import com.finwise.domain.model.AccountType;
import com.finwise.domain.port.in.CreateAccountUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import com.finwise.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepositoryPort accountRepository;
    private final UserRepositoryPort userRepository;

    public CreateAccountService(AccountRepositoryPort accountRepository, UserRepositoryPort userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Account create(CreateAccountCommand command) {
        validateCommand(command);

        if (!userRepository.existsById(command.userId())) {
            throw new ResourceNotFoundException("User not found: " + command.userId());
        }

        Account account = new Account(
                UUID.randomUUID(),
                command.name().trim(),
                parseType(command.type()),
                BigDecimal.ZERO,
                normalizeCurrency(command.currency()),
                Instant.now()
        );

        Account saved = accountRepository.save(account);
        accountRepository.linkUserToAccount(command.userId(), saved.id());
        return saved;
    }

    private void validateCommand(CreateAccountCommand command) {
        if (command.userId() == null) {
            throw new DomainValidationException("User id is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new DomainValidationException("Account name is required");
        }
        if (command.type() == null || command.type().isBlank()) {
            throw new DomainValidationException("Account type is required");
        }
        if (command.currency() == null || command.currency().isBlank()) {
            throw new DomainValidationException("Currency is required");
        }
    }

    private AccountType parseType(String type) {
        try {
            return AccountType.valueOf(type.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new DomainValidationException("Invalid account type: " + type);
        }
    }

    private String normalizeCurrency(String currency) {
        String normalized = currency.trim().toUpperCase(Locale.ROOT);
        if (normalized.length() != 3) {
            throw new DomainValidationException("Currency must be an ISO-4217 code");
        }
        return normalized;
    }
}

