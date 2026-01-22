package com.Raman.credit_risk_engine.dto;

import com.Raman.credit_risk_engine.decision.Decision;
import com.Raman.credit_risk_engine.decision.RiskLevel;

import java.util.List;

/**
 * Internal representation of the final credit risk outcome.
 *
 * This is NOT exposed via REST yet.
 * It will later be used as the API response DTO.
 */
public class CreditRiskResponseDTO {

    private final int creditScore;
    private final RiskLevel riskLevel;
    private final Decision decision;
    private final List<String> reasons;

    public CreditRiskResponseDTO(
            int creditScore,
            RiskLevel riskLevel,
            Decision decision,
            List<String> reasons
    ) {
        this.creditScore = creditScore;
        this.riskLevel = riskLevel;
        this.decision = decision;
        this.reasons = reasons;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Decision getDecision() {
        return decision;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
