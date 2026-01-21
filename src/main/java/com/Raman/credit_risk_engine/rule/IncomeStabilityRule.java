package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.EmploymentType;
import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;

public class IncomeStabilityRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        if (profile.getEmploymentType() == EmploymentType.SALARIED) {
            return new RuleResult(
                    50,
                    "Salaried employment provides stable income"
            );
        }

        return new RuleResult(
                20,
                "Self-employed income considered moderately stable"
        );
    }
}
