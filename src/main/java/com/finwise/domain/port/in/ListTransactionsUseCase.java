package com.finwise.domain.port.in;

import com.finwise.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ListTransactionsUseCase {
    List<Transaction> list(TransactionFilter filter);

    record TransactionFilter(UUID accountId, LocalDate fromDate, LocalDate toDate, String type) {
    }
}

