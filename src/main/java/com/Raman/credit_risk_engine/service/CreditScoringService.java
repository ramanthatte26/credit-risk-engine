package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
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

    /**
     * Spring injects ALL CreditRule beans here.
     * Order is deterministic based on bean registration.
     */
    public CreditScoringService(List<CreditRule> rules) {
        this.rules = List.copyOf(rules); // defensive, immutable
    }

    public ScoringResult calculateScore(
            UserFinancialProfile profile,
            FinancialMetrics metrics
    ) {
        int score = BASE_SCORE;
        List<String> reasons = new ArrayList<>();

        for (CreditRule rule : rules) {
            RuleResult result = rule.evaluate(profile, metrics);
            score += result.getScoreImpact();
            reasons.add(result.getReason());
        }

        return new ScoringResult(score, reasons);
    }
}
