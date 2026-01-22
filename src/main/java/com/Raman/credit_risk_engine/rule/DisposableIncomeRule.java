package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DisposableIncomeRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        BigDecimal disposableIncome = metrics.getDisposableIncome();

        if (disposableIncome.compareTo(BigDecimal.valueOf(25000)) >= 0) {
            return new RuleResult(
                    "DisposableIncomeRule",
                    80,
                    "High disposable income indicates strong repayment capacity"
            );
        }

        if (disposableIncome.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            return new RuleResult(
                    "DisposableIncomeRule",
                    30,
                    "Moderate disposable income provides limited repayment buffer"
            );
        }

        return new RuleResult(
                "DisposableIncomeRule",
                -100,
                "Low disposable income indicates affordability constraints"
        );
    }
}
