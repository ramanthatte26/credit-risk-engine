package com.Raman.credit_risk_engine.metrics;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.exception.RuleEvaluationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FinancialMetricsService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public FinancialMetrics computeMetrics(UserFinancialProfile profile) {

        validateProfile(profile);

        try {
            BigDecimal dti = computeDebtToIncomeRatio(
                    profile.getTotalMonthlyEmis(),
                    profile.getMonthlyIncome()
            );

            BigDecimal disposableIncome = computeDisposableIncome(
                    profile.getMonthlyIncome(),
                    profile.getMonthlyExpenses(),
                    profile.getTotalMonthlyEmis()
            );

            BigDecimal lti = computeLoanToIncomeRatio(
                    profile.getRequestedLoanAmount(),
                    profile.getMonthlyIncome()
            );

            return new FinancialMetrics(dti, disposableIncome, lti);

        } catch (ArithmeticException ex) {
            throw new RuleEvaluationException("Metric computation failed", ex);
        }
    }

    private void validateProfile(UserFinancialProfile profile) {

        BigDecimal income = profile.getMonthlyIncome();
        BigDecimal expenses = profile.getMonthlyExpenses();
        BigDecimal emis = profile.getTotalMonthlyEmis();

        if (income == null || income.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleEvaluationException("Monthly income must be greater than zero");
        }

        if (expenses.compareTo(income) > 0) {
            throw new RuleEvaluationException("Monthly expenses exceed income");
        }

        if (emis.compareTo(income) > 0) {
            throw new RuleEvaluationException("Monthly EMIs exceed income");
        }
    }

    private BigDecimal computeDebtToIncomeRatio(
            BigDecimal totalMonthlyEmis,
            BigDecimal monthlyIncome
    ) {
        return totalMonthlyEmis
                .divide(monthlyIncome, SCALE + 2, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(SCALE, ROUNDING_MODE);
    }

    private BigDecimal computeDisposableIncome(
            BigDecimal monthlyIncome,
            BigDecimal monthlyExpenses,
            BigDecimal totalMonthlyEmis
    ) {
        return monthlyIncome
                .subtract(monthlyExpenses.add(totalMonthlyEmis))
                .setScale(SCALE, ROUNDING_MODE);
    }

    private BigDecimal computeLoanToIncomeRatio(
            BigDecimal requestedLoanAmount,
            BigDecimal monthlyIncome
    ) {
        BigDecimal annualIncome = monthlyIncome.multiply(BigDecimal.valueOf(12));

        if (annualIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleEvaluationException("Annual income must be positive");
        }

        return requestedLoanAmount
                .divide(annualIncome, SCALE + 2, ROUNDING_MODE)
                .setScale(SCALE, ROUNDING_MODE);
    }
}
