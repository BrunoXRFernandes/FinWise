package com.finwise.application.usecase;

import com.finwise.domain.exception.DomainValidationException;
import com.finwise.domain.exception.ResourceNotFoundException;
import com.finwise.domain.model.Account;
import com.finwise.domain.port.in.CreateAccountUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import com.finwise.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {
    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private CreateAccountService service;

    @Test
    void createShouldPersistAndLinkUser() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(accountRepository).linkUserToAccount(any(UUID.class), any(UUID.class));

        Account account = service.create(new CreateAccountUseCase.CreateAccountCommand(userId, "Main", "MAIN", "eur"));

        assertEquals("Main", account.name());
        assertEquals("EUR", account.currency());

        ArgumentCaptor<UUID> accountIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(accountRepository).linkUserToAccount(org.mockito.ArgumentMatchers.eq(userId), accountIdCaptor.capture());
        assertEquals(account.id(), accountIdCaptor.getValue());
    }

    @Test
    void createShouldFailWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.create(new CreateAccountUseCase.CreateAccountCommand(userId, "Main", "MAIN", "USD")));
    }

    @Test
    void createShouldFailOnInvalidCurrency() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        assertThrows(DomainValidationException.class,
                () -> service.create(new CreateAccountUseCase.CreateAccountCommand(userId, "Main", "MAIN", "US")));
    }
}

