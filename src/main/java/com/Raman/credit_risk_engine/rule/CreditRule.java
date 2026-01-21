package com.Raman.credit_risk_engine.rule;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;

public interface CreditRule {

    /**
     * Evaluates a single underwriting rule.
     *
     * @param profile  validated user financial profile (raw facts)
     * @param metrics  derived financial metrics
     * @return RuleResult containing score impact and explanation
     */
    RuleResult evaluate(UserFinancialProfile profile, FinancialMetrics metrics);
}
