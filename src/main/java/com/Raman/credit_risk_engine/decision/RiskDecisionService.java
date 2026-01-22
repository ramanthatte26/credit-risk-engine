package com.Raman.credit_risk_engine.decision;

/**
 * Translates a final credit score into a risk level and business decision.
 *
 * This service does NOT perform scoring or rule evaluation.
 * It strictly maps score -> risk -> decision using fixed thresholds.
 */
public class RiskDecisionService {

    /**
     * Determines the risk level based on the final credit score.
     *
     * @param score final computed credit score
     * @return risk level
     */
    public RiskLevel determineRiskLevel(int score) {

        if (score >= 750) {
            return RiskLevel.LOW;
        }

        if (score >= 600) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.HIGH;
    }

    /**
     * Determines the business decision based on risk level.
     *
     * @param riskLevel derived risk classification
     * @return business decision
     */
    public Decision determineDecision(RiskLevel riskLevel) {

        return switch (riskLevel) {
            case LOW -> Decision.APPROVE;
            case MEDIUM -> Decision.REVIEW;
            case HIGH -> Decision.REJECT;
        };
    }

    /**
     * Convenience method to derive both risk level and decision from score.
     *
     * @param score final computed credit score
     * @return decision outcome
     */
    public DecisionOutcome evaluate(int score) {

        RiskLevel riskLevel = determineRiskLevel(score);
        Decision decision = determineDecision(riskLevel);

        return new DecisionOutcome(score, riskLevel, decision);
    }
}
