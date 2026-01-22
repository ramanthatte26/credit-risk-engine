package com.Raman.credit_risk_engine.service;

import java.util.List;

public class ScoringResult {

    private final int finalScore;
    private final List<String> reasons;

    public ScoringResult(int finalScore, List<String> reasons) {
        this.finalScore = finalScore;
        this.reasons = reasons;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
