package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class DtiRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        BigDecimal dti = metrics.getDebtToIncomeRatio();

        if (dti.compareTo(BigDecimal.valueOf(30)) < 0) {
            return new RuleResult(
                    80,
                    "Low debt-to-income ratio indicates healthy debt levels"
            );
        }

        if (dti.compareTo(BigDecimal.valueOf(50)) <= 0) {
            return new RuleResult(
                    30,
                    "Moderate debt-to-income ratio indicates manageable risk"
            );
        }

        return new RuleResult(
                -100,
                "High debt-to-income ratio indicates heavy existing debt burden"
        );
    }
}
