package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.exception.ResourceNotFoundException;
import com.finwise.domain.model.Transaction;
import com.finwise.domain.model.TransactionType;
import com.finwise.domain.port.in.CreateTransactionUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import com.finwise.domain.port.out.TransactionRepositoryPort;
import com.finwise.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
public class CreateTransactionService implements CreateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;
    private final UserRepositoryPort userRepository;

    public CreateTransactionService(
            TransactionRepositoryPort transactionRepository,
            AccountRepositoryPort accountRepository,
            UserRepositoryPort userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Transaction create(CreateTransactionCommand command) {
        validateCommand(command);

        if (accountRepository.findById(command.accountId()).isEmpty()) {
            throw new ResourceNotFoundException("Account not found: " + command.accountId());
        }
        if (!userRepository.existsById(command.userId())) {
            throw new ResourceNotFoundException("User not found: " + command.userId());
        }

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                command.accountId(),
                command.userId(),
                command.amount(),
                command.category().trim(),
                command.description() == null ? null : command.description().trim(),
                command.date(),
                parseType(command.type())
        );

        return transactionRepository.save(transaction);
    }

    private void validateCommand(CreateTransactionCommand command) {
        if (command.accountId() == null) {
            throw new DomainValidationException("Account id is required");
        }
        if (command.userId() == null) {
            throw new DomainValidationException("User id is required");
        }
        if (command.amount() == null || command.amount().signum() <= 0) {
            throw new DomainValidationException("Amount must be greater than zero");
        }
        if (command.category() == null || command.category().isBlank()) {
            throw new DomainValidationException("Category is required");
        }
        if (command.date() == null) {
            throw new DomainValidationException("Date is required");
        }
        if (command.date().isAfter(LocalDate.now())) {
            throw new DomainValidationException("Date cannot be in the future");
        }
        if (command.type() == null || command.type().isBlank()) {
            throw new DomainValidationException("Transaction type is required");
        }
    }

    private TransactionType parseType(String type) {
        try {
            return TransactionType.valueOf(type.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new DomainValidationException("Invalid transaction type: " + type);
        }
    }
}

