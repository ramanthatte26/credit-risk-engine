package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        int historyMonths = profile.getCreditHistoryLengthMonths();

        if (historyMonths >= 36) {
            return new RuleResult(
                    "CreditHistoryRule",
                    70,
                    "Long credit history improves predictability of borrower behavior"
            );
        }

        if (historyMonths >= 12) {
            return new RuleResult(
                    "CreditHistoryRule",
                    30,
                    "Moderate credit history provides some predictability"
            );
        }

        return new RuleResult(
                "CreditHistoryRule",
                -50,
                "Short credit history reduces confidence in repayment behavior"
        );
    }
}
