package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.rule.RuleResult;

import java.util.List;
import java.util.stream.Collectors;

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

    // Derived view for API / response usage
    public List<String> getReasons() {
        return ruleResults.stream()
                .map(RuleResult::getReason)
                .collect(Collectors.toList());
    }
}
