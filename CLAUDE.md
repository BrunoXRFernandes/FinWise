# FinWise — AI Financial Manager

## Project Overview
Full-stack personal finance management app with AI-powered insights.
Target: couples managing shared finances, tracking expenses, setting goals, and receiving intelligent feedback.

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Security (JWT + OAuth2)
- PostgreSQL
- OpenAI API (AI insights)

### Frontend
- React 18
- Zustand (global state)
- React Query (server state / data fetching)
- Recharts (charts and visualizations)

### Infrastructure
- Docker & Docker Compose (local)
- AWS ECS Fargate (backend)
- AWS RDS PostgreSQL (database)
- AWS S3 + CloudFront (frontend)
- GitHub Actions (CI/CD)

---

## Architecture — Hexagonal (Ports & Adapters)

```
com.finwise
├── domain
│   ├── model          # Pure business entities (no framework dependencies)
│   └── port
│       ├── in         # Use case interfaces (driving ports)
│       └── out        # Repository/service interfaces (driven ports)
├── application
│   └── usecase        # Use case implementations
├── infrastructure
│   ├── persistence    # JPA repositories, adapters
│   ├── ai             # OpenAI adapter
│   └── aws            # S3, Textract adapters
└── presentation
    └── rest           # Controllers, DTOs, mappers
```

### Rules
- Domain has zero dependencies on Spring, JPA, or any framework
- Use cases depend only on domain ports
- Infrastructure implements domain ports
- Controllers depend only on use case interfaces (ports in)
- No business logic in controllers or persistence layer

---

## Domain Models

### User
- id, email, passwordHash, name, createdAt
- belongs to one or two accounts (couple support)

### Account
- id, name, type (MAIN | SAVINGS | INVESTMENTS), balance, currency
- owned by one or more users

### Transaction
- id, amount, category, description, date, type (INCOME | EXPENSE)
- linked to account and user

### Goal
- id, name, targetAmount, currentAmount, deadline, status
- linked to account

### AiInsight
- id, content, type, generatedAt
- linked to account

---

## Database — Main Tables

```sql
users
accounts
user_accounts      -- many-to-many (couple support)
transactions
goals
ai_insights
```

Migrations managed with Flyway under `resources/db/migration`.

---

## Security

- JWT for stateless auth (access token + refresh token)
- OAuth2 for social login (Google)
- Passwords hashed with BCrypt
- All endpoints protected except `/auth/**`

---

## AI Insights — Behaviour

The AI assistant should:
- Analyse spending patterns by category
- Compare current month vs previous months
- Flag unusual expenses
- Give progress updates on goals
- Suggest actionable improvements
- Be aware of couple context (two users, shared account)

Use OpenAI API (`gpt-4o` preferred). Prompt should include:
- Last 3 months of transactions (summarised)
- Active goals and progress
- Account balances
- User-defined context (income, lifestyle preferences)

---

## API Structure

```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/refresh

GET    /api/accounts
POST   /api/accounts
GET    /api/accounts/{id}/summary

GET    /api/transactions
POST   /api/transactions
PUT    /api/transactions/{id}
DELETE /api/transactions/{id}
POST   /api/transactions/upload-invoice   # OCR via Textract

GET    /api/goals
POST   /api/goals
PUT    /api/goals/{id}

GET    /api/insights
POST   /api/insights/generate
```

---

## Environment Variables

```
DB_URL=
DB_USER=
DB_PASSWORD=
JWT_SECRET=
JWT_EXPIRATION_MS=
OPENAI_API_KEY=
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_REGION=
AWS_S3_BUCKET=
```

---

## Code Conventions

- Use `record` for DTOs and value objects where possible
- Use `Optional` properly — no null returns from use cases
- Exceptions: custom domain exceptions, handled globally via `@ControllerAdvice`
- All use case methods return explicit result types or throw domain exceptions
- No `@Autowired` on fields — constructor injection only
- Mappers as separate classes (no mapping logic in controllers or entities)

---

## Testing Strategy

- Unit tests: JUnit 5 + Mockito (use cases and domain logic)
- Integration tests: Testcontainers (PostgreSQL)
- API tests: MockMvc or RestAssured
- Frontend: Cypress (E2E)

---

## Local Setup

```bash
# Start PostgreSQL
docker compose up -d

# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
npm run dev
```

---

## Roadmap

1. Auth (JWT + OAuth2)
2. Account management
3. Transaction CRUD + categories
4. Dashboard + charts
5. AI insights integration
6. Goal tracking
7. Invoice OCR upload
8. Couple/shared account support
9. Notifications
10. Mobile app (React Native)
