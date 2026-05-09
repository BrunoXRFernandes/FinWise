package com.finwise.presentation.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID userId;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM user_accounts");
        jdbcTemplate.update("DELETE FROM transactions");
        jdbcTemplate.update("DELETE FROM accounts");
        jdbcTemplate.update("DELETE FROM users");

        userId = UUID.randomUUID();
        accountId = UUID.randomUUID();

        jdbcTemplate.update(
                "INSERT INTO users (id, email, password_hash, name, created_at) VALUES (?, ?, ?, ?, ?)",
                userId,
                "transaction.user@finwise.test",
                "hash",
                "Transaction User",
                Instant.now()
        );

        jdbcTemplate.update(
                "INSERT INTO accounts (id, name, type, balance, currency, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                accountId,
                "Main Account",
                "MAIN",
                BigDecimal.ZERO,
                "USD",
                Instant.now()
        );

        jdbcTemplate.update(
                "INSERT INTO user_accounts (user_id, account_id) VALUES (?, ?)",
                userId,
                accountId
        );
    }

    @Test
    void createAndListTransactionsWithFilters() throws Exception {
        String payload = """
                {
                  "accountId": "%s",
                  "userId": "%s",
                  "amount": 89.50,
                  "category": "Groceries",
                  "description": "Weekly market",
                  "date": "%s",
                  "type": "EXPENSE"
                }
                """.formatted(accountId, userId, LocalDate.now());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value("Groceries"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));

        mockMvc.perform(get("/api/transactions")
                        .param("accountId", accountId.toString())
                        .param("type", "EXPENSE")
                        .param("fromDate", LocalDate.now().minusDays(1).toString())
                        .param("toDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(accountId.toString()))
                .andExpect(jsonPath("$[0].category").value("Groceries"));
    }

    @Test
    void listShouldFailWhenDateRangeIsInvalid() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("fromDate", LocalDate.now().toString())
                        .param("toDate", LocalDate.now().minusDays(1).toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("fromDate must be before or equal to toDate"));
    }
}

