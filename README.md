# CryoNex Banking — Customer Service (CILM)

**Customer Identity & Lifecycle Management** microservice — the first module of **CryoNex Banking**, a multi-module Spring Boot microservices platform.

This service owns everything about a bank customer's identity and lifecycle: onboarding, address, contact details, KYC document upload and verification, nominees, preferences, and a full audit trail — from account creation through status changes.

Built end-to-end as a set of 46 REST endpoints across 8 business domains, fully tested via Postman, and secured with JWT-based role authorization.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Domains & Features](#domains--features)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [API Reference](#api-reference)
- [Notable Engineering Decisions](#notable-engineering-decisions)

---

## Overview

CryoNex Banking is designed as a multi-module Maven project, where each business capability lives in its own Spring Boot microservice. `cryonex-customer-service` is the first module, fully built — it handles Customer Identity & Lifecycle Management (CILM):

- Customer onboarding, search, update, and status management
- Address and contact management
- KYC document upload/download and a full verification state machine
- Nominee management with share-percentage validation
- Customer preferences (language, notification, marketing consent)
- A complete, append-only audit trail across every domain, exportable as PDF or Excel
- JWT-based authentication with role-based endpoint authorization

`cryonex-account-service` is under development in the same parent project. Further modules (transaction service, etc.) are planned.

---

## Architecture

```
cryonex-banking/                    (parent Maven project)
├── pom.xml                         (manages shared dependency versions)
├── cryonex-customer-service/       (this module)
│   ├── controller/                 8 REST controllers
│   ├── service/                    9 services (incl. StorageService, no controller)
│   ├── repository/                 8 Spring Data JPA repositories
│   ├── entity/                     8 JPA entities + BaseEntity + IdSequence
│   ├── dto/
│   │   ├── request/                Request DTOs (validated, never expose entities)
│   │   └── response/                Response DTOs
│   ├── enums/                      14 domain enums
│   ├── exception/                  Custom exceptions + centralized handler
│   ├── security/                   JWT filter
│   ├── config/                     Spring Security, JPA auditing config
│   └── util/                       ID generation, JWT utility
└── cryonex-account-service/        (in development)
```

**Design principles followed throughout:**

- **Vertical-slice development** — each domain (Customer, Address, Contact, Document, KYC, Nominee, Preferences, Audit) was built and Postman-tested completely — DTOs → Service → Controller — before starting the next.
- **DTOs everywhere** — entities are never returned directly from a controller. Request DTOs omit fields a client shouldn't control (e.g. `customerId`, `panNumber` on update); response DTOs omit internal fields (e.g. a document's file path).
- **Centralized error handling** — every endpoint returns a consistent `ApiResponse` envelope (`status`, `errorCode`, `message`, `data`), whether it succeeds or fails.
- **Atomic writes** — every service method that performs more than one database write is `@Transactional`, so a failure partway through rolls back cleanly instead of leaving partial data.
- **Append-only audit trail** — every state-changing action across every domain writes a new, uniquely-ID'd `CustomerAudit` record. History is never overwritten.

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot, Spring MVC |
| Data Layer | Spring Data JPA, Hibernate, MySQL |
| Security | Spring Security, JWT (jjwt 0.12.6) |
| Validation | Jakarta Bean Validation |
| File Export | iText 7 (PDF), Apache POI (Excel) |
| Testing | JUnit 5, Mockito, Spring Boot Test (concurrency test against a separate MySQL test database) |
| Build | Maven (multi-module) |
| Tooling | IntelliJ IDEA, Postman, Git/GitHub |

---

## Domains & Features

| # | Domain | Description |
|---|--------|-------------|
| 1 | **Customer Core** | Onboarding, get, dynamic search (JPA Specifications, incl. a join-based mobile filter), update, status change, soft delete |
| 2 | **Address** | Add/update/delete addresses; enforces exactly one primary address per customer; a primary address cannot be deleted |
| 3 | **Contact** | Mobile/email uniqueness, alternate-mobile cross-field validation, preferred contact mode |
| 4 | **Document** | Multipart file upload/download (PDF/JPEG/PNG, 5MB limit), one document per type per customer, pluggable local-disk storage layer |
| 5 | **KYC** | A genuine state machine: `PENDING → VERIFIED`, `PENDING → REJECTED → (resubmit) → PENDING`. Verification requires both PAN and Aadhaar to be uploaded. A KYC record is auto-created as `PENDING` the first time any document is uploaded |
| 6 | **Nominee** | Requires the customer's KYC to be `VERIFIED` before a nominee can be added or verified; total share percentage across all nominees is capped at 100% (`BigDecimal` arithmetic); a verified nominee cannot be deleted |
| 7 | **Preferences** | Language, communication mode, and independent email/SMS/marketing toggles; one preference record per customer |
| 8 | **Audit** | Full history, single-record lookup, dynamic search (JPA Specifications with optional filters + pagination), and PDF/Excel export of the complete audit trail |

**46 REST endpoints in total**, each tested via Postman during development.

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- MySQL 8+

### 1. Clone the repository

```bash
git clone https://github.com/va-run04/cryonex-banking.git
cd cryonex-banking
```

### 2. Configure the database

Copy the example configuration and fill in your own credentials:

```bash
cp cryonex-customer-service/src/main/resources/application-example.yml \
   cryonex-customer-service/src/main/resources/application.yml
```

Edit `application.yml` with your MySQL username/password. The schema is created automatically on first run (`ddl-auto: update`) — no manual SQL required.

### 3. Build and run

```bash
mvn clean install -pl cryonex-customer-service
cd cryonex-customer-service
mvn spring-boot:run
```

The service starts on **`http://localhost:8081`**.

### 4. Run the tests

MySQL must be running. The concurrency test uses a separate `cryonex_test` database, created automatically with the same credentials as your `application.yml`, so it never touches your real data.

```bash
mvn test -pl cryonex-customer-service
```

---

## Authentication

Every endpoint (except `/auth/**`) requires a valid JWT with an appropriate role, enforced via Spring Security `@PreAuthorize`.

> Since this module doesn't yet have a dedicated `auth-service`, `/auth/mock-login` issues a real, signed JWT for whatever username/role you request — intended for local development and testing only.

### Get a token

```
POST /auth/mock-login
Content-Type: application/json

{
  "username": "ravi.kumar",
  "role": "KYC_OFFICER"
}
```

Response:
```json
{
  "status": "SUCCESS",
  "message": "Mock token generated successfully.",
  "data": {
    "token": "eyJhbGciOi...",
    "username": "ravi.kumar",
    "role": "KYC_OFFICER"
  }
}
```

Use the token as a Bearer token on subsequent requests: `Authorization: Bearer <token>`.

### Roles

| Role | Access |
|---|---|
| `BANK_EMPLOYEE` | Customer, Address, Contact, Document, Nominee (CRUD), Preferences |
| `KYC_OFFICER` | KYC verify/reject, Nominee verification |
| `OPERATIONS_EXECUTIVE` | Audit — history, search, single record, export |
| `ADMIN` | Full access — allowed on every endpoint |

---

## API Reference

All responses follow a consistent envelope:

```json
{
  "status": "SUCCESS | FAILED",
  "errorCode": "DOMAIN_CODE | null",
  "message": "human-readable message",
  "data": { }
}
```

Error codes follow a `[DOMAIN_PREFIX]_[NUMBER]` convention (`CUS_101`, `ADDR_001`, `KYC_004`, `AUTH_403`, etc.).

### Endpoint groups

| Base path | Domain |
|---|---|
| `/api/v1/customers` | Customer Core |
| `/api/v1/customers/{customerId}/addresses` | Address |
| `/api/v1/customers/{customerId}/contacts` | Contact |
| `/api/v1/customers/{customerId}/documents` | Document |
| `/api/v1/customers/{customerId}/kyc` | KYC |
| `/api/v1/customers/{customerId}/nominees` | Nominee |
| `/api/v1/customers/{customerId}/preferences` | Preferences |
| `/api/v1/customers/{customerId}/audit` | Audit |
| `/auth/mock-login` | Auth (mock, temporary) |

A full Postman collection covering all 46 endpoints is included in this repo — see [`postman/CryoNex-CustomerService.postman_collection.json`](postman/CryoNex-CustomerService.postman_collection.json). Import it into Postman, set the `baseUrl` variable to `http://localhost:8081`, and get a token from `/auth/mock-login` to start testing immediately.

---

## Notable Engineering Decisions

Two real, non-trivial bugs were found and fixed during development (the first one in three stages) — identified through testing and code review, not by design from the start.

**1. ID generation: a collision bug, then two races in its own fix**

The original ID strategy derived the next ID from `repository.count()`. Because `count()` reflects the *current* row count — which drops when a row is deleted — this could regenerate an ID that had already been issued to a still-existing row. In practice, this caused a re-uploaded PAN document to silently **overwrite** an existing Aadhaar document, since both ended up with the same generated ID.

**First fix:** a dedicated `IdSequence` table — one row per entity type, tracking a monotonically increasing counter completely independent of the actual data tables. IDs are never reused, even across deletions.

**Second bug, found by reviewing that fix:** it read the counter, incremented it and saved it with no lock, so two simultaneous requests could both read the same value and issue the same ID. `@Transactional` makes the steps all-or-nothing, but it does not stop two transactions from running at the same time. A concurrency test (`IdGeneratorUtilConcurrencyTest`) fires 20 simultaneous calls at a separate `cryonex_test` database. In one run before the fix, only **3 of the 20 IDs were unique**; the exact number varies from run to run.

**Second fix:** the counter row is now read with a pessimistic write lock (`@Lock(PESSIMISTIC_WRITE)`, which becomes `SELECT ... FOR UPDATE`), so simultaneous requests queue on the row. The same test now passes consistently, with all 20 IDs unique.

**Third bug, found by reviewing that fix and confirmed with a test:** the lock only works on a row that exists. When the counter row for an entity didn't exist yet, `SELECT ... FOR UPDATE` had nothing to lock, so simultaneous first calls all tried to create the row at once. A second test (`IdGeneratorUtilFirstCallConcurrencyTest`), run against an empty counter table, showed **9 of 20 calls failing with deadlock errors** (11 succeeded) before the fix.

**Third fix:** `generateId` now first runs an atomic insert-if-absent (MySQL `INSERT ... ON DUPLICATE KEY UPDATE`, which does nothing if the row already exists), then takes the lock on a row that is guaranteed to exist. The same test now passes consistently: 20 calls, 0 failures, 20 unique IDs.

**Trade-offs:** the lock is held until the caller's transaction commits, so ID generation for the same entity type is serialised — fine at this scale. A very high-volume system would use database sequences or pre-allocated ID blocks instead. The insert-if-absent adds one extra statement per ID, and it ties this code to MySQL syntax.

**2. Missing transaction boundaries (data-consistency bug)**

Several service methods performed two writes — the main entity update, followed by an audit log entry — without `@Transactional`. When the second write failed (due to an unrelated bug), the first write had already committed, leaving the customer's data changed with **no corresponding audit record** — a silent compliance gap.

**Fix:** `@Transactional` applied to every service method performing two or more writes, so a failure anywhere rolls back the entire operation.

---

**Varun Kumar**
[GitHub](https://github.com/va-run04) · [LinkedIn](https://www.linkedin.com/in/varun-kumar-vk004/)