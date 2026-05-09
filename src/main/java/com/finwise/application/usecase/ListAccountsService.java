package com.finwise.application.usecase;

import com.finwise.domain.model.Account;
import com.finwise.domain.port.in.ListAccountsUseCase;
import com.finwise.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListAccountsService implements ListAccountsUseCase {
    private final AccountRepositoryPort accountRepository;

    public ListAccountsService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> listByUserId(UUID userId) {
        return accountRepository.findAllByUserId(userId);
    }
}

