package com.finwise.presentation.rest;

import com.finwise.domain.model.Account;
import com.finwise.domain.port.in.CreateAccountUseCase;
import com.finwise.domain.port.in.ListAccountsUseCase;
import com.finwise.domain.port.in.UpdateAccountUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/accounts")
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountRestMapper mapper;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            ListAccountsUseCase listAccountsUseCase,
            UpdateAccountUseCase updateAccountUseCase,
            AccountRestMapper mapper
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        Account account = createAccountUseCase.create(mapper.toCreateCommand(request));
        return mapper.toResponse(account);
    }

    @GetMapping
    public List<AccountResponse> list(@RequestParam @NotNull UUID userId) {
        return listAccountsUseCase.listByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @PutMapping("/{accountId}")
    public AccountResponse update(@PathVariable UUID accountId, @Valid @RequestBody UpdateAccountRequest request) {
        Account account = updateAccountUseCase.update(accountId, mapper.toUpdateCommand(request));
        return mapper.toResponse(account);
    }
}

