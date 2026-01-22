package com.Raman.credit_risk_engine.rule;

public class RuleResult {

    private final String ruleName;
    private final int scoreImpact;
    private final String reason;

    public RuleResult(String ruleName, int scoreImpact, String reason) {
        this.ruleName = ruleName;
        this.scoreImpact = scoreImpact;
        this.reason = reason;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public String getReason() {
        return reason;
    }
}
