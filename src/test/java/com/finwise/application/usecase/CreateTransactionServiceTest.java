package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.exception.ResourceNotFoundException;
import com.finwise.domain.model.Account;
import com.finwise.domain.model.AccountType;
import com.finwise.domain.model.Transaction;
import com.finwise.domain.port.in.CreateTransactionUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import com.finwise.domain.port.out.TransactionRepositoryPort;
import com.finwise.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTransactionServiceTest {
    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private CreateTransactionService service;

    @Test
    void createShouldPersistTransaction() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(new Account(
                accountId,
                "Main",
                AccountType.MAIN,
                BigDecimal.ZERO,
                "USD",
                Instant.now()
        )));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = service.create(new CreateTransactionUseCase.CreateTransactionCommand(
                accountId,
                userId,
                new BigDecimal("12.34"),
                "Food",
                "Lunch",
                LocalDate.now(),
                "expense"
        ));

        assertEquals("Food", transaction.category());
        assertEquals("EXPENSE", transaction.type().name());
    }

    @Test
    void createShouldFailWhenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.create(new CreateTransactionUseCase.CreateTransactionCommand(
                        accountId,
                        userId,
                        new BigDecimal("12.34"),
                        "Food",
                        null,
                        LocalDate.now(),
                        "EXPENSE"
                )));
    }

    @Test
    void createShouldFailWhenAmountIsInvalid() {
        assertThrows(DomainValidationException.class,
                () -> service.create(new CreateTransactionUseCase.CreateTransactionCommand(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        BigDecimal.ZERO,
                        "Food",
                        null,
                        LocalDate.now(),
                        "EXPENSE"
                )));
    }
}

