# Gym Membership Management System (GMMS)

## Build & Run

The project uses OpenAPI Generator to produce controller interfaces and model classes at build time.
These generated sources do not exist in the repository, so the IDE will show errors until you compile at least once.

```bash
./mvnw compile  
```

Run the app:

```bash
./mvnw spring-boot:run 
```

### Run tests

```bash
./mvnw test
```

---

## URLs

The app runs on port **3000**.

| What | URL |
|------|-----|
| Swagger UI | http://localhost:3000/swagger |
| OpenAPI JSON | http://localhost:3000/v3/api-docs |
| H2 Console | http://localhost:3000/h2 |

### H2 Console connection settings

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:gmmsdb;MODE=PostgreSQL` |
| Username | `sa` |
| Password | `password` |

---

## API Overview

Base path: `/api/v1`

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/gyms` | Create a gym |
| `GET` | `/gyms` | List all gyms |
| `POST` | `/gyms/{gymId}/plans` | Create a membership plan |
| `GET` | `/gyms/{gymId}/plans` | List plans for a gym |
| `POST` | `/plans/{planId}/members` | Register a member |
| `GET` | `/members` | List all members (includes plan name, gym name, status) |
| `PATCH` | `/members/{memberId}/cancel` | Cancel a membership |
| `GET` | `/gyms/revenue` | Monthly revenue report per gym and currency |


