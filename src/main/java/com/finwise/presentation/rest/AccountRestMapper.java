package com.finwise.presentation.rest;

import com.finwise.domain.model.Account;
import com.finwise.domain.port.in.CreateAccountUseCase;
import com.finwise.domain.port.in.UpdateAccountUseCase;
import org.springframework.stereotype.Component;

@Component
public class AccountRestMapper {
    public CreateAccountUseCase.CreateAccountCommand toCreateCommand(CreateAccountRequest request) {
        return new CreateAccountUseCase.CreateAccountCommand(
                request.userId(),
                request.name(),
                request.type(),
                request.currency()
        );
    }

    public UpdateAccountUseCase.UpdateAccountCommand toUpdateCommand(UpdateAccountRequest request) {
        return new UpdateAccountUseCase.UpdateAccountCommand(request.name());
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.id(),
                account.name(),
                account.type().name(),
                account.balance(),
                account.currency(),
                account.createdAt()
        );
    }
}

