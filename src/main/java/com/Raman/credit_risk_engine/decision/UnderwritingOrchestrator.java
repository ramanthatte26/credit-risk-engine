package com.Raman.credit_risk_engine.decision;

import com.Raman.credit_risk_engine.service.CreditScoringService;
import com.Raman.credit_risk_engine.service.ScoringResult;

/**
 * Orchestrates scoring and decisioning into a single underwriting result.
 *
 * This service contains NO business rules.
 * It only coordinates existing engines.
 */
public class UnderwritingOrchestrator {

    private final CreditScoringService scoringService;
    private final RiskDecisionService riskDecisionService;

    public UnderwritingOrchestrator(
            CreditScoringService scoringService,
            RiskDecisionService riskDecisionService
    ) {
        this.scoringService = scoringService;
        this.riskDecisionService = riskDecisionService;
    }

    public UnderwritingResult underwrite(
            ScoringResult scoringResult
    ) {

        int score = scoringResult.getFinalScore();

        RiskLevel riskLevel = riskDecisionService.determineRiskLevel(score);
        Decision decision = riskDecisionService.determineDecision(riskLevel);

        return new UnderwritingResult(
                score,
                riskLevel,
                decision,
                scoringResult.getReasons()
        );
    }
}
