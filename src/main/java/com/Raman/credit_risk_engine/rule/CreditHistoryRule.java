package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;

public class CreditHistoryRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        int historyMonths = profile.getCreditHistoryLengthMonths();

        if (historyMonths >= 36) {
            return new RuleResult(
                    70,
                    "Long credit history improves predictability of borrower behavior"
            );
        }

        if (historyMonths >= 12) {
            return new RuleResult(
                    30,
                    "Moderate credit history provides some predictability"
            );
        }

        return new RuleResult(
                -50,
                "Short credit history reduces confidence in repayment behavior"
        );
    }
}
