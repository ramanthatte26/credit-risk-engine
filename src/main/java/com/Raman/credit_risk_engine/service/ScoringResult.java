package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.rule.RuleResult;
import java.util.List;

public class ScoringResult {

    private final int finalScore;
    private final List<RuleResult> ruleResults;

    public ScoringResult(int finalScore, List<RuleResult> ruleResults) {
        this.finalScore = finalScore;
        this.ruleResults = ruleResults;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public List<RuleResult> getRuleResults() {
        return ruleResults;
    }
}
