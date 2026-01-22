package com.Raman.credit_risk_engine.decision;

/**
 * Holds the final outcome of the decisioning process.
 *
 * This is an internal model, not an API response.
 */
public class DecisionOutcome {

    private final int score;
    private final RiskLevel riskLevel;
    private final Decision decision;

    public DecisionOutcome(int score, RiskLevel riskLevel, Decision decision) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.decision = decision;
    }

    public int getScore() {
        return score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Decision getDecision() {
        return decision;
    }
}
