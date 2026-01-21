package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;

import java.math.BigDecimal;

public class DisposableIncomeRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        BigDecimal disposableIncome = metrics.getDisposableIncome();

        if (disposableIncome.compareTo(BigDecimal.valueOf(25000)) >= 0) {
            return new RuleResult(
                    80,
                    "High disposable income indicates strong repayment capacity"
            );
        }

        if (disposableIncome.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            return new RuleResult(
                    30,
                    "Moderate disposable income provides limited repayment buffer"
            );
        }

        return new RuleResult(
                -100,
                "Low disposable income indicates affordability constraints"
        );
    }
}
