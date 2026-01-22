package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.exception.RuleEvaluationException;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import com.Raman.credit_risk_engine.rule.CreditRule;
import com.Raman.credit_risk_engine.rule.RuleResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditScoringService {

    private static final int BASE_SCORE = 1000;
    private final List<CreditRule> rules;

    public CreditScoringService(List<CreditRule> rules) {
        this.rules = rules;
    }

    public ScoringResult calculateScore(
            UserFinancialProfile profile,
            FinancialMetrics metrics
    ) {
        int score = BASE_SCORE;
        List<RuleResult> ruleResults = new ArrayList<>();

        for (CreditRule rule : rules) {
            try {
                RuleResult result = rule.evaluate(profile, metrics);
                score += result.getScoreImpact();
                ruleResults.add(result);
            } catch (Exception ex) {
                throw new RuleEvaluationException(
                        "Failed to evaluate rule: " + rule.getClass().getSimpleName(),
                        ex
                );
            }
        }

        return new ScoringResult(score, ruleResults);
    }
}
