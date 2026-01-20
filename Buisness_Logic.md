Below is a **precise, industry-aligned functional specification written in plain English**, suitable for a design document, interview discussion, or project README. Nothing is omitted. Nothing is watered down. This reflects **how real FinTech underwriting microservices are defined at the start of a project**.

---

## 1. What This System Does

This system is a **backend credit risk evaluation microservice**.

Its responsibility is to:

* Accept a user’s financial profile as input
* Calculate critical financial health metrics
* Apply an explainable, rule-based credit scoring model
* Produce a **credit score**, **risk classification**, and **decision**
* Return **human-readable reasons** justifying the decision
* Persist the complete evaluation for **audit, compliance, and analytics**

The system **does not disburse loans** and **does not make subjective judgments**.
It strictly evaluates risk based on measurable financial data, exactly as done in early-stage or rule-based underwriting engines used by FinTechs and NBFCs.

This microservice is designed to be:

* Deterministic
* Explainable
* Auditable
* Extensible for future ML-based models

---

## 2. Input Parameters (User Financial Profile)

The API receives a structured financial snapshot of the user at the time of evaluation.

### Input Fields

1. **Monthly Income**

    * The user’s total recurring monthly income
    * Used as the base for affordability and risk calculations

2. **Monthly Expenses**

    * Fixed and variable living expenses
    * Excludes EMIs
    * Used to calculate disposable income

3. **Existing EMIs**

    * Total monthly EMI obligations across all active loans
    * Critical indicator of current debt burden

4. **Past Loan Defaults (Count)**

    * Number of historical loan defaults
    * Strong predictor of credit risk

5. **Credit History Length (Months)**

    * Total duration of active credit history
    * Indicates borrower maturity and predictability

6. **Employment Type**

    * Salaried or Self-employed
    * Used as a proxy for income stability

7. **Age**

    * Used for eligibility checks and future policy constraints
    * Not directly scored in this version, but preserved for audit

8. **Requested Loan Amount**

    * Loan amount applied for in the current request
    * Used to detect over-leverage

All inputs are treated as **immutable facts** for that evaluation cycle and are stored as-is.

---

## 3. Derived Metrics (Critical Financial Indicators)

Before scoring begins, the system computes derived metrics.
These metrics translate raw inputs into **risk signals**.

### a) Debt-to-Income Ratio (DTI)

**Definition**
DTI measures how much of a user’s income is already committed to debt.

**Formula**
DTI = (Total Monthly EMI / Monthly Income) × 100

**Interpretation**

* **< 30%** → Healthy debt load
* **30–50%** → Risky but manageable
* **> 50%** → Over-leveraged and high risk

DTI is one of the most important underwriting metrics globally.

---

### b) Disposable Income

**Definition**
Disposable income represents how much money remains after meeting all monthly obligations.

**Formula**
Disposable Income = Monthly Income − (Monthly Expenses + Existing EMIs)

**Interpretation**

* Positive disposable income indicates repayment capacity
* Low or negative disposable income is a strong rejection signal

Negative disposable income is treated as an **automatic red flag**, regardless of score.

---

### c) Loan-to-Income Ratio (LTI)

**Definition**
LTI evaluates whether the requested loan size is reasonable relative to income.

**Formula**
LTI = Requested Loan Amount / Annual Income

**Purpose**

* Detects over-borrowing
* Prevents approving loans that cannot realistically be serviced

LTI is not directly scored here but is stored and returned for policy enforcement and future models.

---

## 4. Scoring Rules (Explainable, Rule-Based Model)

The system uses a **transparent, additive scoring model**.

### Base Score

* Every user starts with a **base score of 1000**

### Income Stability

* Salaried with stable income → **+50**
* Self-employed → **+20**

Reason: Salaried income is statistically more predictable.

---

### Debt-to-Income Impact

* DTI < 30% → **+80**
* DTI between 30–50% → **+30**
* DTI > 50% → **−100**

Reason: Higher DTI directly reduces repayment capacity.

---

### Past Loan Defaults

* 0 defaults → **+100**
* 1 default → **−100**
* 2 or more defaults → **−250**

Reason: Past behavior is one of the strongest predictors of future risk.

---

### Credit History Length

* ≥ 36 months → **+70**
* 12–36 months → **+30**
* < 12 months → **−50**

Reason: Longer credit history increases confidence in borrower behavior.

---

### Disposable Income

* ≥ ₹25,000 → **+80**
* ₹10,000–25,000 → **+30**
* < ₹10,000 → **−100**

Reason: Higher disposable income improves affordability margin.

---

## 5. Risk Buckets and Final Decision

After applying all scoring rules, the final score is mapped to a risk category.

### Classification Table

| Score Range | Risk Level | Decision |
| ----------- | ---------- | -------- |
| ≥ 750       | LOW        | APPROVE  |
| 600–749     | MEDIUM     | REVIEW   |
| < 600       | HIGH       | REJECT   |

### Meaning

* **APPROVE**: Safe to auto-approve
* **REVIEW**: Needs human or secondary verification
* **REJECT**: Risk exceeds acceptable thresholds

This mirrors real-world credit funnels used in production systems.

---

## 6. Why Explainability Matters

Explainability is **non-negotiable** in financial systems.

### Reasons:

1. **Regulatory Compliance**

    * Regulators require justification for credit decisions
    * Black-box decisions are unacceptable

2. **Customer Trust**

    * Users must understand why they were approved or rejected

3. **Operational Transparency**

    * Support teams need clear reasons to handle disputes

4. **Interview & Product Maturity Signal**

    * Demonstrates understanding beyond “score calculation”
    * Shows system thinking, not just coding

The system therefore returns **explicit, human-readable reasons** for every decision.

---

## 7. Why an Audit Trail Is Mandatory

Every evaluation is persisted with full context.

### Stored Data Includes:

* Raw input snapshot
* Derived metrics (DTI, Disposable Income, LTI)
* Final score
* Risk level and decision
* Decision reasons
* Timestamp

### Why This Is Required:

1. **Compliance**

    * Mandatory for financial audits and regulator inspections

2. **Debugging**

    * Enables investigation of incorrect or disputed decisions

3. **Model Improvement**

    * Historical data is used to refine rules or train ML models

4. **Enterprise Readiness**

    * Any system without auditability is not production-grade

An underwriting system **without history is incomplete**.

---

## Final Note (Important)

This definition is:

* Realistic
* Industry-aligned
* Interview-safe
* Production-oriented

This is the **correct foundation** to build the project on.
Once this is implemented faithfully, everything else (API design, database schema, code) becomes straightforward and defensible.
