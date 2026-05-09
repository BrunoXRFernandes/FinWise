package com.finwise.infrastructure.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccountJpaEntity {
    @EmbeddedId
    private UserAccountId id;

    protected UserAccountJpaEntity() {
    }

    public UserAccountJpaEntity(UserAccountId id) {
        this.id = id;
    }

    public UserAccountId getId() {
        return id;
    }
}

