# FinWise

Backend-first personal finance API with a local-first setup.

## Sprint 1 Scope

- Minimal auth (`/api/auth/register`, `/api/auth/login`)
- Accounts endpoints (`/api/accounts`)
- Transactions endpoints (`/api/transactions`)
- PostgreSQL + Flyway migrations

## Required Environment Variables

The application uses defaults for local development, but you can override them:

- `DB_URL` (default `jdbc:postgresql://localhost:5432/finwise`)
- `DB_USER` (default `finwise`)
- `DB_PASSWORD` (default `finwise`)
- `JWT_SECRET` (default `change-me-in-prod`)
- `JWT_EXPIRATION_MS` (default `3600000`)

## Run Locally

1. Start PostgreSQL and create the `finwise` database.
2. Run the app:

```powershell
.\mvnw.cmd spring-boot:run
```

3. Run tests:

```powershell
.\mvnw.cmd test
```
