package com.Raman.credit_risk_engine.metrics;

import java.math.BigDecimal;

public class FinancialMetrics {

    /**
     * Debt-to-Income Ratio (DTI)
     * Unit: Percentage (e.g., 42.5 means 42.5%)
     * Must never be null
     */
    private BigDecimal debtToIncomeRatio;

    /**
     * Disposable Income (Monthly)
     * Unit: Absolute currency value (monthly)
     * Can be negative, must never be null
     */
    private BigDecimal disposableIncome;

    /**
     * Loan-to-Income Ratio (LTI)
     * Unit: Ratio (e.g., 3.5 means loan is 3.5x annual income)
     * Must never be null
     */
    private BigDecimal loanToIncomeRatio;

    public FinancialMetrics(
            BigDecimal debtToIncomeRatio,
            BigDecimal disposableIncome,
            BigDecimal loanToIncomeRatio
    ) {
        this.debtToIncomeRatio = debtToIncomeRatio;
        this.disposableIncome = disposableIncome;
        this.loanToIncomeRatio = loanToIncomeRatio;
    }

    public BigDecimal getDebtToIncomeRatio() {
        return debtToIncomeRatio;
    }

    public BigDecimal getDisposableIncome() {
        return disposableIncome;
    }

    public BigDecimal getLoanToIncomeRatio() {
        return loanToIncomeRatio;
    }
}
