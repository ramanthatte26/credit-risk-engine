package com.Raman.credit_risk_engine.decision;

import java.util.List;

/**
 * Internal representation of a complete underwriting outcome.
 *
 * This is NOT an API response.
 * It is produced after scoring and decisioning are complete.
 */
public class UnderwritingResult {

    private final int finalScore;
    private final RiskLevel riskLevel;
    private final Decision decision;
    private final List<String> reasons;

    public UnderwritingResult(
            int finalScore,
            RiskLevel riskLevel,
            Decision decision,
            List<String> reasons
    ) {
        this.finalScore = finalScore;
        this.riskLevel = riskLevel;
        this.decision = decision;
        this.reasons = reasons;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Decision getDecision() {
        return decision;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
