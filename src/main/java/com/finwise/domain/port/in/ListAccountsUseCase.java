package com.finwise.domain.port.in;

import com.finwise.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface ListAccountsUseCase {
    List<Account> listByUserId(UUID userId);
}

