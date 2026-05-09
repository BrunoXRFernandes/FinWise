package com.finwise.presentation.rest;

import com.finwise.domain.model.Transaction;
import com.finwise.domain.port.in.CreateTransactionUseCase;
import com.finwise.domain.port.in.ListTransactionsUseCase;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class TransactionRestMapper {
    public CreateTransactionUseCase.CreateTransactionCommand toCreateCommand(CreateTransactionRequest request) {
        return new CreateTransactionUseCase.CreateTransactionCommand(
                request.accountId(),
                request.userId(),
                request.amount(),
                request.category(),
                request.description(),
                request.date(),
                request.type()
        );
    }

    public ListTransactionsUseCase.TransactionFilter toFilter(
            UUID accountId,
            LocalDate fromDate,
            LocalDate toDate,
            String type
    ) {
        return new ListTransactionsUseCase.TransactionFilter(accountId, fromDate, toDate, type);
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.id(),
                transaction.accountId(),
                transaction.userId(),
                transaction.amount(),
                transaction.category(),
                transaction.description(),
                transaction.date(),
                transaction.type().name()
        );
    }
}

