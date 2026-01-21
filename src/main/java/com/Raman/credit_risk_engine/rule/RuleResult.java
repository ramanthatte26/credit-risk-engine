package com.Raman.credit_risk_engine.rule;

public class RuleResult {

    private final int scoreImpact;
    private final String reason;

    public RuleResult(int scoreImpact, String reason) {
        this.scoreImpact = scoreImpact;
        this.reason = reason;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public String getReason() {
        return reason;
    }
}
