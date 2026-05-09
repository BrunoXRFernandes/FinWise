package com.finwise.infrastructure.persistence;

import com.finwise.domain.model.Account;
import com.finwise.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {
    public AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
                account.id(),
                account.name(),
                account.type(),
                account.balance(),
                account.currency(),
                account.createdAt()
        );
    }

    public Account toDomain(AccountJpaEntity entity) {
        return new Account(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getBalance(),
                entity.getCurrency(),
                entity.getCreatedAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.id(),
                user.email(),
                user.passwordHash(),
                user.name(),
                user.createdAt()
        );
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getName(),
                entity.getCreatedAt()
        );
    }
}

