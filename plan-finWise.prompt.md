## Plan: FinWise Backend Delivery Roadmap

Build the app in thin vertical slices while establishing architecture and delivery guardrails. Prioritize backend APIs for `accounts` and `transactions` first, with minimal auth to support protected flows, and optimize for rapid local delivery.

### Scope Decision
- Selected direction: **Option C (narrowed MVP)**
- First implementation focus: `transactions` + `accounts` (backend)
- Delivery mode: rapid local delivery
- Deferred for now: AI insights, OCR, goals, OAuth2, Docker, CI hardening

### Steps 3-6 steps, 5-20 words each
1. Lock scoped MVP in `plan-finWise.prompt.md`: backend-first, accounts and transactions, rapid local delivery.
2. Prepare hexagonal module skeleton under `src/main/java/com/finwise` for domain, application, infrastructure, presentation.
3. Add local-first persistence baseline in `application.properties` and initial Flyway migrations for users, accounts, transactions.
4. Implement `accounts` slice first: create/list/update endpoints with use cases, repositories, and integration tests.
5. Implement `transactions` slice next: create/list/filter endpoints with account linkage and validation rules.
6. Add minimal auth (`/api/auth/register`, `/api/auth/login`) and secure account/transaction endpoints with focused tests.

### Immediate Sprint Goals
1. Ship account create/list/update APIs with persistence and validation.
2. Ship transaction create/list/filter APIs linked to accounts.
3. Ship minimal JWT auth and role-agnostic endpoint protection.
4. Validate with repository, use-case, and REST integration tests.

### Further Considerations
1. Package migration timing: move from `com.example.finwise` to `com.finwise` now or after first sprint?
2. Keep local speed high: use Postgres + Flyway only, defer Docker/CI until API contracts stabilize.
3. Define done criteria early: endpoint coverage, schema stability, and test pass thresholds.
