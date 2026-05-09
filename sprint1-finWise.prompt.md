## Plan: Sprint 1 Backend Core APIs

Deliver a backend-first MVP in thin vertical slices: set architecture guardrails, then ship `accounts` and `transactions` APIs with minimal JWT auth and focused automated tests. This sequence keeps local delivery fast, minimizes rework risk, and aligns to `plan-finWise.prompt.md` priorities while deferring non-critical features (AI/OCR/goals/OAuth2/CI hardening).

### Objective
Ship a locally runnable Spring Boot backend that supports protected account and transaction flows (`register/login` + account create/list/update + transaction create/list/filter) using PostgreSQL + Flyway, with hexagonal structure and baseline test coverage.

### Scope In / Out
- In scope: backend only; hexagonal skeleton under `src/main/java`; Flyway schema for users/accounts/transactions; minimal JWT auth; REST APIs for accounts and transactions; integration and use-case tests.
- Out of scope: AI insights, OCR upload, goals, OAuth2 social login, Docker/CI hardening, frontend integration, couple-sharing edge cases beyond schema-ready modeling.

### Steps 3-6 steps, 5-20 words each
1. Finalize package strategy and sprint contract in `plan-finWise.prompt.md`.
2. Create hexagonal module skeleton and shared error handling paths.
3. Add Postgres/Flyway baseline and first schema migrations.
4. Implement `accounts` vertical slice with tests.
5. Implement `transactions` vertical slice with filters and validations.
6. Add minimal JWT auth and secure non-auth endpoints.

### Tickets (10)

1. **FW-01 - Sprint contract + package decision (S)**
   - Files: `plan-finWise.prompt.md`, `pom.xml`, `src/main/java/com/example/finwise/FinWiseApplication.java`
   - Acceptance criteria: Sprint scope locked; explicit decision on `com.example.finwise` vs `com.finwise`; no ambiguity on deferred items.

2. **FW-02 - Hexagonal folder scaffold (M)**
   - Files: `src/main/java/com/finwise/domain`, `src/main/java/com/finwise/application`, `src/main/java/com/finwise/infrastructure`, `src/main/java/com/finwise/presentation`
   - Symbols: `port.in`, `port.out`, `usecase`
   - Acceptance criteria: Skeleton packages exist; dependency direction documented; controllers depend on input ports only.

3. **FW-03 - Dependency and config baseline (M)**
   - Files: `pom.xml`, `src/main/resources/application.properties`
   - Acceptance criteria: JPA, Flyway, Validation, Security/JWT libs added; env-driven DB/JWT properties defined; app starts with missing optional features deferred.

4. **FW-04 - Flyway v1 schema for users/accounts/transactions (M)**
   - Files: `src/main/resources/db/migration/V1__init_core.sql`
   - Acceptance criteria: Tables `users`, `accounts`, `user_accounts`, `transactions` created with PK/FK/indexes; migration runs clean on empty DB.

5. **FW-05 - Accounts domain + use cases (M)**
   - Files: `src/main/java/com/finwise/domain/model/Account*`, `src/main/java/com/finwise/domain/port/in/*Account*`, `src/main/java/com/finwise/application/usecase/*Account*`
   - Symbols: `CreateAccountUseCase`, `ListAccountsUseCase`, `UpdateAccountUseCase`
   - Acceptance criteria: Business rules for account type/currency/balance validated in use cases; no framework dependency in domain.

6. **FW-06 - Accounts persistence + REST adapters (L)**
   - Files: `src/main/java/com/finwise/infrastructure/persistence/*Account*`, `src/main/java/com/finwise/presentation/rest/*Account*`
   - Symbols: `POST /api/accounts`, `GET /api/accounts`, `PUT /api/accounts/{id}`
   - Acceptance criteria: Endpoints return expected status codes/payloads; DTOs are `record`; mapper isolated from controller logic.

7. **FW-07 - Transactions domain + use cases (M)**
   - Files: `src/main/java/com/finwise/domain/model/Transaction*`, `src/main/java/com/finwise/domain/port/in/*Transaction*`, `src/main/java/com/finwise/application/usecase/*Transaction*`
   - Symbols: `CreateTransactionUseCase`, `ListTransactionsUseCase`
   - Acceptance criteria: Transaction must link to existing account; type/amount/date/category validations enforced in application/domain layer.

8. **FW-08 - Transactions persistence + REST filters (L)**
   - Files: `src/main/java/com/finwise/infrastructure/persistence/*Transaction*`, `src/main/java/com/finwise/presentation/rest/*Transaction*`
   - Symbols: `POST /api/transactions`, `GET /api/transactions` (with filter params)
   - Acceptance criteria: List supports at least account/date/type filters; invalid filter inputs return standardized validation errors.

9. **FW-09 - Minimal auth (register/login + JWT guard) (L)**
   - Files: `src/main/java/com/finwise/presentation/rest/*Auth*`, `src/main/java/com/finwise/infrastructure/security/*`, `src/main/resources/application.properties`
   - Symbols: `POST /api/auth/register`, `POST /api/auth/login`
   - Acceptance criteria: Password hashed with BCrypt; login returns JWT; `/api/auth/**` public, account/transaction endpoints protected.

10. **FW-10 - Test suite for sprint scope (M)**
   - Files: `src/test/java/com/finwise/**`, `pom.xml`, `README.md`
   - Symbols: MockMvc/API integration tests, use-case unit tests, Flyway migration startup test
   - Acceptance criteria: Green tests for auth + accounts + transactions happy path and key failures; reproducible local run instructions documented.

### Dependency Order
1. FW-01
2. FW-02 -> FW-03 -> FW-04
3. FW-05 -> FW-06
4. FW-07 -> FW-08
5. FW-09 (after FW-06/FW-08 endpoints exist)
6. FW-10 (runs continuously, finalized last)

### Risks / Mitigations
1. Package migration churn may slow delivery; decide once in FW-01.
2. Overbuilding auth too early can block core APIs; keep JWT scope minimal.
3. Schema rework from unclear ownership rules; use `user_accounts` join table in V1.
4. Test setup drag can reduce velocity; keep focused integration matrix.

### Definition of Done
1. `register/login`, account create/list/update, and transaction create/list/filter endpoints run locally.
2. Non-auth endpoints require JWT; unauthorized requests return consistent 401/403.
3. Flyway migration initializes schema from zero without manual SQL.
4. Hexagonal layering is respected across domain, application, infrastructure, presentation.
5. Unit and integration tests for sprint scope pass in local Maven run.
6. `README.md` contains minimal startup steps and required env vars.

### Further Considerations
1. Package path now: A) migrate to `com.finwise` in Sprint 1, B) keep `com.example.finwise` until Sprint 2.
2. Transaction filters minimum: A) account+date, B) account+date+type, C) include category in Sprint 1.
3. After option decisions, convert this into implementation-ready ticket checklist.
