# Credit Risk Scoring Engine

A production-style backend service that evaluates a user’s financial profile, computes credit risk using explainable business rules, and produces an auditable underwriting decision.

This project mirrors how **real FinTech and banking systems** perform early-stage credit underwriting — with deterministic scoring, transparent reasoning, and compliance-ready audit trails.

---

## 1️⃣ Project Overview

### What Problem Does This Solve?

In lending and FinTech platforms, deciding **whether to approve a loan is not subjective** — it is a structured, rule-driven process.

This service answers three critical questions:

1. **How risky is this borrower?**
2. **Why is the borrower considered risky or safe?**
3. **What business action should be taken?**

The Credit Risk Scoring Engine automates this evaluation in a way that is:

- Deterministic (same input → same output)
- Explainable (every score has reasons)
- Auditable (every decision is stored with justification)
- Extensible (rules and scoring can evolve safely)

---

## 2️⃣ Why Credit Risk Scoring Matters in FinTech

In real-world lending systems:

- Blind approvals lead to defaults and losses
- Black-box decisions fail regulatory scrutiny
- Unexplainable scores cannot be defended to auditors or regulators

This system is designed to reflect **industry realities**:

- Regulators require _decision explainability_
- Risk teams require _rule-level audit trails_
- Engineers require _clean separation of concerns_

This project intentionally avoids shortcuts such as:

- Hard-coded approvals
- Magic numbers without justification
- Mixing business logic with controllers or persistence

---

## 3️⃣ High-Level Capabilities

The system performs the following end-to-end flow:

1. Accepts a user’s financial profile via API
2. Computes derived financial metrics (DTI, Disposable Income, LTI)
3. Applies independent underwriting rules
4. Aggregates rule impacts into a final credit score
5. Converts score into risk category and business decision
6. Persists decision and full audit trail
7. Returns a clean, explainable API response

Every step is isolated, testable, and documented.

---

## 4️⃣ Key Design Principles

This project follows **enterprise backend design principles**:

- **Separation of Concerns**  
  Metrics, rules, scoring, decisioning, persistence, and APIs are isolated.

- **Fail-Fast Validation**  
  Invalid financial profiles are rejected early.

- **Explainability First**  
  Every rule returns a human-readable reason.

- **Deterministic Logic**  
  No randomness, no hidden state, no side effects.

- **Audit & Compliance Ready**  
  Decisions and reasons are stored separately.

These principles make the system suitable for real-world FinTech use cases.

---

## 5️⃣ What This Project Is (and Is Not)

### ✅ This Project IS:

- A rule-based credit underwriting engine
- A realistic FinTech backend system
- Fully explainable and auditable
- Recruiter- and interview-ready

### ❌ This Project Is NOT:

- A toy demo
- A machine learning model
- A frontend-heavy application
- A shortcut-based approval system

The focus is correctness, clarity, and real-world relevance.

---

## 6️⃣ Core Business Logic

This system follows a **deterministic, rule-based underwriting model**.

> **Same input will always produce the same output.**  
> There is no randomness, no learning, and no hidden state.

This makes the system:

- Predictable
- Auditable
- Easy to reason about
- Suitable for regulated environments

---

## 7️⃣ Input Data Model

The service evaluates a borrower using the following inputs:

- Monthly Income
- Monthly Expenses
- Total Monthly EMIs
- Employment Type (Salaried / Self-Employed)
- Past Loan Defaults
- Credit History Length (months)
- Requested Loan Amount
- Age

These inputs are validated at the API boundary to ensure that:

- Income is positive
- Expenses and EMIs do not exceed income
- Required fields are always present

Invalid profiles never enter the scoring engine.

---

## 8️⃣ Derived Financial Metrics

From the raw inputs, the system computes key financial indicators used by lenders worldwide.

### Debt-to-Income Ratio (DTI)

DTI = (Total Monthly EMIs / Monthly Income) × 100

- Indicates how much of the income is already committed to debt
- Higher DTI → higher risk

---

### Disposable Income

Disposable Income = Monthly Income − (Monthly Expenses + Total Monthly EMIs)

- Measures repayment capacity
- Negative or low values indicate affordability risk

---

### Loan-to-Income Ratio (LTI)

Annual Income = Monthly Income × 12

LTI = Requested Loan Amount / Annual Income

- Indicates whether the loan size is reasonable relative to income

All calculations use controlled precision and rounding to ensure consistency.

---

## 9️⃣ Rule-Based Scoring Model

The system starts with a **base score of 1000**.

Each underwriting rule:

- Evaluates the profile independently
- Produces a **score impact** (positive or negative)
- Produces a **human-readable reason**

Rules do not know:

- The base score
- The final score
- Any other rules

This ensures isolation and extensibility.

### Implemented Rules

|                       Rule |                        Purpose |
| -------------------------: | -----------------------------: |
|      Income Stability Rule | Evaluates employment stability |
|                   DTI Rule |           Assesses debt burden |
|     Disposable Income Rule |         Assesses affordability |
| Credit History Length Rule |        Assesses predictability |
|       Default History Rule |       Assesses behavioral risk |

Score impacts are **additive**, not weighted or averaged.

---

## 🔟 Risk Buckets & Decisions

After scoring, the final credit score is mapped to risk and decision.

### Risk Classification

| Credit Score | Risk Level |
| -----------: | ---------: |
|        ≥ 750 |        LOW |
|    600 – 749 |     MEDIUM |
|        < 600 |       HIGH |

---

### Business Decisions

| Risk Level | Decision | Meaning                      |
| ---------: | -------: | ---------------------------- |
|        LOW |  APPROVE | Safe to auto-approve         |
|     MEDIUM |   REVIEW | Requires manual underwriting |
|       HIGH |   REJECT | Risk too high to proceed     |

> **Why REVIEW exists:**  
> Not all risk can be captured by rules. Medium-risk profiles require human judgment.

---

## 1️⃣1️⃣ Explainability & Auditability

Every evaluation produces:

- Final credit score
- Risk level
- Decision
- Full list of rule reasons

Additionally, the system persists:

- One **CreditAssessment** (final outcome)
- Multiple **AssessmentAudit** records (rule-level justification)

This ensures:

- Regulatory compliance
- Decision traceability
- Easy debugging and analysis

---

## 1️⃣2️⃣ System Architecture

The application follows a clean, layered architecture, separating concerns clearly.

High-Level Flow (diagram)

```text
                      +-----------------------------+
                      |  Client (Postman / Frontend)|
                      +-------------+---------------+
                                    |
                                    v
                      +-------------+---------------+
                      |        REST Controller      |
                      +-------------+---------------+
                                    |
                                    v
                      +-------------+---------------+
                      | CreditRiskEvaluationService |
                      |       (Orchestrator)        |
                      +-------------+---------------+
                                    |
                                    v
          +-----------------------------------------------+
          | FinancialMetricsService -> Rule Engine        |
          | -> CreditScoringService -> RiskDecisionService|
          +-----------------------------------------------+
                                    |
                                    v
                      +-------------+---------------+
                      |    Persistence Layer (JPA)  |
                      +-------------+---------------+
                                    |
                                    v
                      +-------------+---------------+
                      | Audit Trail & Decision Store|
                      +-----------------------------+
```

### Layer Responsibilities

- Controller Layer

  - Exposes REST API
  - Validates input
  - Converts HTTP requests/responses
  - Contains no business logic

- Orchestration Service (CreditRiskEvaluationService)

  - Coordinates all engines
  - Ensures correct execution order
  - Manages transaction boundaries
  - Persists results and audits

- Metrics Engine (FinancialMetricsService)

  - Computes derived financial metrics
  - Enforces fail-fast validation
  - Rejects impossible financial profiles

- Rule Engine

  - Applies independent underwriting rules
  - Produces score impact + human-readable reason
  - Stateless and deterministic

- Scoring Engine

  - Owns base score (1000)
  - Aggregates rule impacts
  - Produces the final credit score

- Decision Engine

  - Converts score → risk level → decision
  - Contains no scoring logic

- Persistence Layer
  - Stores final decisions and assessment snapshots
  - Stores rule-level audit data
  - Repositories contain no business logic

---

## 1️⃣3️⃣ Exception Handling & Hardening

The system is hardened to behave like a regulated FinTech platform. All runtime errors are handled consistently and mapped to clear HTTP responses.

### Exception Strategy

| Scenario              | Exception                       | HTTP Status |
| --------------------- | ------------------------------- | ----------: |
| Validation failure    | MethodArgumentNotValidException |         400 |
| Business rule failure | RuleEvaluationException         |         422 |
| Unexpected error      | Exception                       |         500 |

All exceptions are handled by a single `GlobalExceptionHandler`.

This ensures:

- No stack traces leak to clients
- Error responses are consistent and machine-parseable
- Failures are explicit and traceable for audit

---

## 1️⃣4️⃣ REST API Contract

### Endpoint

POST /credit-risk/evaluate

### Request Body (JSON)

```json
{
  "monthlyIncome": 80000,
  "monthlyExpenses": 25000,
  "totalMonthlyEmis": 15000,
  "pastLoanDefaults": 0,
  "creditHistoryLengthMonths": 48,
  "employmentType": "SALARIED",
  "age": 30,
  "requestedLoanAmount": 500000
}
```

### Successful Response

```json
{
  "creditScore": 1380,
  "riskLevel": "LOW",
  "decision": "APPROVE",
  "reasons": [
    "Salaried employment provides stable income",
    "Low debt-to-income ratio indicates healthy debt levels",
    "No past loan defaults indicate reliable repayment behavior",
    "Long credit history improves predictability of borrower behavior",
    "High disposable income indicates strong repayment capacity"
  ]
}
```

### Error Response (Example)

```json
{
  "timestamp": "2026-01-22T10:15:30.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Monthly income must be greater than zero"
}
```

Notes

- All validation errors return 400.
- Business rule violations that prevent scoring return 422.
- Unexpected server errors return 500.
- Responses include human-readable reasons for explainability and an audit trail is persisted for each decision.

---

## 1️⃣5️⃣ Persistence & Audit Trail

### CreditAssessment

Stores the final underwriting outcome:

- Credit score
- Risk level
- Decision
- Timestamp
- Request snapshot (input + derived metrics)

### AssessmentAudit

Stores rule-level justification for explainability and audit:

- Rule name
- Score impact (delta)
- Reason (human-readable)
- Timestamp

This design supports:

- Regulatory audits
- Explainability requirements
- Historical analysis and reporting

---

## 1️⃣6️⃣ How to Run the Project

### Prerequisites

- Java 17+
- Maven
- MySQL (or another configured datasource)
- Postman (for testing)

### Run Steps (example)

```bash
git clone <repository-url>
cd credit-risk-engine
mvn clean install
mvn spring-boot:run
```

Application starts at:

http://localhost:8080

---

## 1️⃣7️⃣ Why This Project Matters

This project demonstrates:

- Real-world FinTech underwriting logic
- Clean architecture and separation of concerns
- Deterministic, explainable decisioning
- Enterprise-grade exception handling
- Audit-ready persistence design

It mirrors how actual credit scoring engines are built inside:

- Banks
- NBFCs
- FinTech lending platforms

---

## 1️⃣8️⃣ Future Enhancements

- Web frontend: build a responsive UI (React/Vue/Angular), add auth and admin dashboards — convert this into a web app
- ML-assisted (hybrid): augment rule-based scoring with ML (training, versioning, explainability)
- Config-driven rules & feature flags: runtime tuning and safe rollouts
- Risk-based pricing & advanced decisioning: price per risk bucket, counter-offers and dynamic terms
- Observability & reporting: dashboards, alerts, audit logs

---
