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


## User Financial Profile – Input Contract

This section defines **why each input field exists**, **how it contributes to credit risk evaluation**, and **what the system does if the input is missing or invalid**.  
Every field in this contract is intentional and justifiable from a **business**, **risk**, and **regulatory** standpoint.

---

### 1. Monthly Income

**Why this input exists**  
Monthly income represents the borrower’s primary source of repayment capacity.

**Why it matters for credit risk**
- All affordability metrics depend on income
- Used directly in:
   - Debt-to-Income (DTI) calculation
   - Disposable income calculation
   - Loan-to-Income (LTI) calculation
- Higher income generally increases repayment resilience

**If missing or invalid**
- Credit risk cannot be assessed without income
- Derived metrics become meaningless
- **System action:** Reject request as invalid input

**Conceptual example**  
Monthly income → ability to repay consistently

---

### 2. Monthly Expenses

**Why this input exists**  
Expenses represent unavoidable living costs that reduce available income for loan repayment.

**Why it matters for credit risk**
- Used to compute disposable income
- High expenses reduce financial flexibility
- Two users with the same income can have very different risk profiles based on expenses

**If missing or invalid**
- Disposable income cannot be computed accurately
- Risk of overstating affordability
- **System action:** Reject request or flag for review based on policy

**Conceptual example**  
Monthly expenses → limits on repayment capacity

---

### 3. Existing EMIs

**Why this input exists**  
Existing EMIs indicate current debt obligations.

**Why it matters for credit risk**
- Directly impacts:
   - Debt-to-Income (DTI)
   - Disposable income
- High existing EMIs increase default probability
- Industry-standard measure of over-leverage

**If missing or invalid**
- DTI cannot be calculated correctly
- Risk of approving already over-burdened borrowers
- **System action:** Reject request as incomplete

**Conceptual example**  
Existing EMIs → current debt burden

---

### 4. Past Loan Defaults (Count)

**Why this input exists**  
Past defaults capture historical repayment behavior.

**Why it matters for credit risk**
- One of the strongest predictors of future default
- Indicates willingness and discipline to repay, not just ability
- Penalized heavily in scoring due to high risk correlation

**If missing or invalid**
- Behavioral risk assessment becomes incomplete
- **System action:** Treat as high risk or reject based on policy

**Conceptual example**  
Past defaults → behavioral risk

---

### 5. Credit History Length (Months)

**Why this input exists**  
Credit history length measures how long the borrower has interacted with credit products.

**Why it matters for credit risk**
- Longer history improves predictability
- Short or thin credit files increase uncertainty
- Widely used by bureaus and lenders

**If missing or invalid**
- Risk model loses confidence calibration
- **System action:** Penalize score or mark for manual review

**Conceptual example**  
Credit history length → predictability of borrower behavior

---

### 6. Employment Type (Salaried / Self-employed)

**Why this input exists**  
Employment type acts as a proxy for income stability.

**Why it matters for credit risk**
- Salaried income is generally more predictable
- Self-employed income can be volatile
- Impacts scoring but not eligibility by itself

**If missing or invalid**
- Income stability cannot be assessed
- **System action:** Apply conservative scoring or reject input

**Conceptual example**  
Employment type → income stability risk

---

### 7. Age

**Why this input exists**  
Age is required for eligibility checks and compliance.

**Why it matters for credit risk**
- Ensures borrower meets legal lending age
- Used for future policy rules (e.g., retirement proximity)
- Stored for audit and regulatory reasons

**If missing or invalid**
- Legal and compliance risk
- **System action:** Reject request immediately

**Conceptual example**  
Age → regulatory and eligibility constraint

---

### 8. Requested Loan Amount

**Why this input exists**  
This defines the lender’s exposure for the current application.

**Why it matters for credit risk**
- Used to calculate Loan-to-Income (LTI)
- Detects over-borrowing relative to income
- Higher exposure increases loss severity

**If missing or invalid**
- Cannot evaluate affordability or exposure
- **System action:** Reject request as invalid

**Conceptual example**  
Requested loan amount → exposure risk


## TASK 8 — DTO → Entity Mapping Strategy (Manual)

### Purpose of the Mapping Layer

The mapping layer is responsible for converting **API-facing DTOs** into **persistence-ready entities**.  
It acts as a controlled boundary between external input and internal state.

This layer:
- Does not contain business logic
- Does not perform calculations
- Does not make decisions

Its only responsibility is **safe and explicit data transfer**.

---

### Overall Mapping Flow

1. Client sends a credit risk evaluation request
2. Controller receives `CreditRiskRequestDTO`
3. DTO validation is executed at the API boundary
4. Mapper converts DTO into `UserFinancialProfile`
5. Entity is passed to the service layer for persistence and further processing

At no point does the controller or client interact directly with the entity.

---

### Field Mapping Rules

**Mapped from DTO to Entity:**
- Monthly income
- Monthly expenses
- Total monthly EMIs
- Past loan defaults
- Credit history length (months)
- Employment type
- Age
- Requested loan amount

**Not mapped (system-managed fields):**
- Primary key
- Record creation timestamp
- Any audit or decision-related fields

System-managed fields are populated internally by the application.

---

### Validation Boundary Rule

- DTO validation ensures all required fields are present and valid
- Mapping assumes the DTO is already valid
- Mapping does not re-validate or transform values

If validation fails, mapping is never executed.

---

### Error Handling Responsibility

- Mapping errors are treated as **programming errors**
- Any unexpected nulls or invalid values indicate a contract breach or upstream bug
- Such errors should not be handled as user input errors

---

### Why Manual Mapping Is Used

Manual mapping is chosen at this stage to:
- Maintain full control over field assignments
- Prevent accidental data leakage
- Enable easier debugging
- Provide clear explanations during interviews

Automation tools may be introduced later after the domain stabilizes.

---

### Design Principle

> The mapper copies data; it does not think.

No business rules  
No scoring logic  
No derived metric calculations  
No side effects
