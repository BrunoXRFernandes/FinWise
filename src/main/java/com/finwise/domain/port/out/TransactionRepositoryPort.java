package com.finwise.domain.port.out;

import com.finwise.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);

    List<Transaction> findByFilter(UUID accountId, LocalDate fromDate, LocalDate toDate, String type);
}

