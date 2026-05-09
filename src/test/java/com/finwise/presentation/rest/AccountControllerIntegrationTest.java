package com.finwise.presentation.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID userId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM user_accounts");
        jdbcTemplate.update("DELETE FROM transactions");
        jdbcTemplate.update("DELETE FROM accounts");
        jdbcTemplate.update("DELETE FROM users");

        userId = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO users (id, email, password_hash, name, created_at) VALUES (?, ?, ?, ?, ?)",
                userId,
                "user@finwise.test",
                "hash",
                "Test User",
                Instant.now()
        );
    }

    @Test
    void createAndListAccounts() throws Exception {
        String payload = """
                {
                  "userId": "%s",
                  "name": "Joint Account",
                  "type": "MAIN",
                  "currency": "USD"
                }
                """.formatted(userId);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Joint Account"))
                .andExpect(jsonPath("$.currency").value("USD"));

        mockMvc.perform(get("/api/accounts").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("MAIN"))
                .andExpect(jsonPath("$[0].name").value("Joint Account"));
    }

    @Test
    void updateAccountName() throws Exception {
        String payload = """
                {
                  "userId": "%s",
                  "name": "Travel Fund",
                  "type": "SAVINGS",
                  "currency": "EUR"
                }
                """.formatted(userId);

        String response = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        UUID accountId = UUID.fromString(json.get("id").asText());

        mockMvc.perform(put("/api/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Travel 2026\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Travel 2026"));
    }
}

