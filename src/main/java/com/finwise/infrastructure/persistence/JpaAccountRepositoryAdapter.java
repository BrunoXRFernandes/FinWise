package com.finwise.infrastructure.persistence;

import com.finwise.domain.model.Account;
import com.finwise.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaAccountRepositoryAdapter implements AccountRepositoryPort {
    private final SpringDataAccountRepository accountRepository;
    private final SpringDataUserAccountRepository userAccountRepository;
    private final AccountPersistenceMapper mapper;

    public JpaAccountRepositoryAdapter(
            SpringDataAccountRepository accountRepository,
            SpringDataUserAccountRepository userAccountRepository,
            AccountPersistenceMapper mapper
    ) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = mapper.toEntity(account);
        AccountJpaEntity saved = accountRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void linkUserToAccount(UUID userId, UUID accountId) {
        userAccountRepository.save(new UserAccountJpaEntity(new UserAccountId(userId, accountId)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Account> findAllByUserId(UUID userId) {
        return accountRepository.findAllByUserId(userId).stream().map(mapper::toDomain).toList();
    }
}

