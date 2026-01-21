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

## Derived Financial Metrics

This section defines the **derived financial metrics** used by the credit risk engine.  
These metrics are calculated from raw user inputs and represent the **true risk indicators** used in underwriting.

In real-world lending systems:
- Raw inputs are never used directly for decisions
- Decisions are always based on derived, normalized metrics
- Metrics must be mathematically clear, explainable, and reusable

---

### 1. Debt-to-Income Ratio (DTI)

**Definition**  
Debt-to-Income Ratio measures the percentage of a borrower’s income that is already committed to existing debt obligations.

**Formula**  
DTI = (Total Monthly EMIs / Monthly Income) × 100

**Why it matters**
- Indicates how much financial capacity is already consumed by debt
- High DTI leaves little room for new repayment obligations
- One of the most widely used affordability metrics across banks and FinTech lenders

**Industry Thresholds**
- **< 30%** → Healthy debt load
- **30% – 50%** → Elevated risk, careful review required
- **> 50%** → High risk, borrower likely over-leveraged

**Why higher DTI = higher risk**
- Less disposable income remains for unexpected expenses
- Higher probability of missed payments under stress
- Strong correlation with default rates in credit portfolios

---

### 2. Disposable Income

**Definition**  
Disposable income represents the amount of money remaining after all mandatory monthly obligations are met.

**Formula**  
Disposable Income = Monthly Income − (Monthly Expenses + Total Monthly EMIs)

**Why negative disposable income is dangerous**
- Indicates the borrower is already spending more than they earn
- Any additional loan increases financial strain immediately
- Repayment would depend on external support or future income assumptions

**Why this is a red-flag metric**
- Negative or very low disposable income signals unsustainable finances
- Often used as an automatic rejection or hard-stop condition
- Independent of credit score or past behavior

Disposable income directly reflects **real repayment capacity**, not historical behavior.

---

### 3. Loan-to-Income Ratio (LTI)

**Definition**  
Loan-to-Income Ratio measures the size of the requested loan relative to the borrower’s annual income.

**Formula**  
LTI = Requested Loan Amount / Annual Income

**Why annual income is used**
- Loan obligations typically span multiple years
- Annual income provides a stable baseline for long-term exposure assessment
- Aligns loan size with realistic earning capacity over time

**Why over-borrowing is risky**
- Large loans relative to income increase loss severity
- Higher chance of default during income disruption
- Indicates potential misalignment between borrower expectations and affordability

LTI helps detect **exposure risk**, even when monthly affordability appears acceptable.

---

### Summary

Derived financial metrics:
- Normalize raw input data
- Enable fair comparison across borrowers
- Form the foundation for all downstream risk scoring and decisions

If these metrics are incorrect or poorly defined, **all credit decisions become unreliable**.

## Derived Financial Metrics — Calculation Rules (STRICT)

This section defines the **exact calculation rules** for all derived financial metrics used in the system.

These rules are:
- Deterministic
- Non-negotiable
- Version-locked

Any change to these rules requires a **formal version update**, as downstream scoring and decisions depend on them.

---

### 1️⃣ Debt-to-Income Ratio (DTI) — Calculation Rules

**Preconditions**
- Monthly income **must be greater than 0**
- If monthly income is 0 or invalid, metric computation must fail immediately

**Formula**
DTI = (Total Monthly EMIs / Monthly Income) × 100

**Precision Rule**
- DTI is calculated using **decimal arithmetic**
- Precision: **2 decimal places**
- Rounding mode: **HALF_UP**

**Post-conditions**
- DTI must be a finite numeric value
- DTI is expressed as a **percentage**, not a ratio

**Interpretation Note**
Higher DTI indicates a higher proportion of income already committed to debt, increasing credit risk.

---

### 2️⃣ Disposable Income — Calculation Rules

**Formula**
Disposable Income = Monthly Income − (Monthly Expenses + Total Monthly EMIs)

**Precision Rule**
- Calculated using **decimal arithmetic**
- Precision: **2 decimal places**
- Rounding mode: **HALF_UP**

**Post-conditions**
- Disposable income may be **positive, zero, or negative**
- All three cases are meaningful and must be preserved

**Risk Signaling Rules**
- **Negative disposable income**
   - Indicates unsustainable financial profile
   - Must be flagged as a **critical risk signal**
- **Zero disposable income**
   - Indicates no buffer for repayment stress
   - Must be treated as **extreme caution**

Disposable income directly reflects real repayment capacity and must never be ignored.

---

### 3️⃣ Loan-to-Income Ratio (LTI) — Calculation Rules

**Preconditions**
- Annual income **must never be zero**
- Annual income is derived from monthly income

**Annual Income Formula**
Annual Income = Monthly Income × 12

**Loan-to-Income Formula**
Loan-to-Income Ratio = Requested Loan Amount / Annual Income

**Precision Rule**
- Calculated using **decimal arithmetic**
- Precision: **2 decimal places**
- Rounding mode: **HALF_UP**

**Post-conditions**
- LTI must be a finite numeric value
- LTI is expressed as a **ratio**, not a percentage

**Interpretation Note**
Higher LTI indicates higher exposure relative to earning capacity, increasing loss severity risk.

---

### Rule Stability Guarantee

- These calculation rules are **frozen**
- All scoring, rules, and decisions must rely on these definitions
- Incorrect or inconsistent metric calculations invalidate the entire risk model

Metric correctness is **non-negotiable**.


## Derived Financial Metrics — Failure Strategy (STRICT)

This section defines how the system handles **invalid or impossible financial profiles** during metric computation.

In real underwriting systems:
- Impossible profiles are rejected **early**
- Such profiles are not scored
- Such profiles are not classified as “high risk”
- They are treated as **invalid input**, not risky input

This system follows the same principle.

---

### Chosen Strategy

✅ **Reject with exception (Fail Fast)**

Invalid profiles are rejected **during metric computation**, before any scoring or decision logic is invoked.

They are **not** passed downstream as high-risk cases.

---

### Failure Conditions and System Behavior

#### 1️⃣ Monthly Income = 0

**Reason**
- Division by income is required for DTI and LTI
- Zero income makes metric computation mathematically impossible

**System Action**
- Immediately reject the profile
- Throw a validation / computation exception
- Stop further processing

**Rationale**
A borrower with zero income cannot be evaluated for affordability.

---

#### 2️⃣ Monthly Expenses > Monthly Income

**Reason**
- Indicates the borrower spends more than they earn
- Disposable income becomes structurally negative

**System Action**
- Immediately reject the profile
- Throw a validation / computation exception
- Stop further processing

**Rationale**
This represents an unsustainable financial profile, not a risk tier.

---

#### 3️⃣ Total Monthly EMIs > Monthly Income

**Reason**
- Debt obligations exceed total income
- DTI exceeds 100%, making affordability invalid

**System Action**
- Immediately reject the profile
- Throw a validation / computation exception
- Stop further processing

**Rationale**
Such a profile cannot support additional credit under any circumstances.

---

### Why Rejection Is Preferred Over “High Risk”

- **High risk** implies the profile is valid but risky
- These cases are **invalid**, not risky
- Scoring invalid data corrupts model integrity
- Early rejection simplifies downstream logic

---

### Design Principle

> **Invalid data is rejected.  
Valid but risky data is scored.**

This separation ensures:
- Mathematical correctness
- Clean metric computation
- Predictable system behavior
- Regulatory defensibility

---

### Rule Stability Notice

- This failure strategy is **frozen**
- Any change requires a versioned update
- Downstream services must rely on this behavior


### Day 4 - Task 2 (Rule Output Contract)
## Rule Output Contract (STRICT)

This section defines the **standard output format** for every underwriting rule in the system.

Each rule evaluates a valid financial profile (via derived metrics) and returns **its independent opinion**.

Rules do not:
- Know the final score
- Know approval or rejection status
- Mutate any input data
- Depend on other rules

They only express **impact** and **reason**.

---

### What a Rule Returns

Each rule returns a **RuleResult** containing exactly two elements:

---

### 1️⃣ Score Impact

**Definition**  
The numerical effect this rule has on the overall credit score.

**Characteristics**
- Integer value
- Can be **positive**, **negative**, or **zero**
- Fixed and deterministic for a given condition

**Interpretation**
- Positive value → reduces perceived risk
- Negative value → increases perceived risk
- Zero → neutral impact

**Important Constraints**
- A rule never computes the final score
- A rule never sees other rules’ impacts
- A rule only declares *its own* impact

**Examples**
- `+80` → strong positive signal
- `-100` → significant risk signal
- `0` → informational / no impact

---

### 2️⃣ Reason (Human-Readable Explanation)

**Definition**  
A clear English sentence explaining **why** the rule produced its score impact.

**Purpose**
- Regulatory explainability
- Customer transparency
- Debugging and auditability

**Characteristics**
- Plain English
- Non-technical
- References the **metric condition**, not internal logic
- Explains *why risk changes*, not just *what value exists*

**Must NOT**
- Mention score totals
- Mention approval or rejection
- Mention internal thresholds explicitly unless meaningful to humans

**Examples**
- “Debt-to-income ratio is above 50%, indicating high existing debt burden”
- “Disposable income is low, leaving limited buffer for new repayments”
- “Long credit history indicates stable borrowing behavior”

---

### Rule Output Example (Conceptual)

Impact: `-100`  
Reason: `"Debt-to-income ratio above 50%"`

---

### Rule Purity Guarantee

Every rule must be:
- **Pure** (no side effects)
- **Deterministic** (same input → same output)
- **Read-only** (never mutates metrics or profile)

Rules express opinions.  
Aggregation and decisions happen later.

---

### Design Principle (Non-Negotiable)

> **Rules do not decide outcomes.  
They only contribute signals.**

This contract is frozen and must be followed by all current and future rules.
