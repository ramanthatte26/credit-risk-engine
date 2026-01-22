package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import org.springframework.stereotype.Component;

@Component
public class DefaultHistoryRule implements CreditRule {

    @Override
    public RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics) {

        int defaults = profile.getPastLoanDefaults();

        if (defaults == 0) {
            return new RuleResult(
                    "DefaultHistoryRule",
                    100,
                    "No past loan defaults indicate reliable repayment behavior"
            );
        }

        if (defaults == 1) {
            return new RuleResult(
                    "DefaultHistoryRule",
                    -100,
                    "One past loan default indicates increased behavioral risk"
            );
        }

        return new RuleResult(
                "DefaultHistoryRule",
                -250,
                "Multiple past loan defaults indicate high behavioral risk"
        );
    }
}
