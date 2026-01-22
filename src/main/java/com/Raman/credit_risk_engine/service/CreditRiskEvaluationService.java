package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.decision.Decision;
import com.Raman.credit_risk_engine.decision.RiskDecisionService;
import com.Raman.credit_risk_engine.decision.RiskLevel;
import com.Raman.credit_risk_engine.dto.CreditRiskRequestDTO;
import com.Raman.credit_risk_engine.dto.CreditRiskResponseDTO;
import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import com.Raman.credit_risk_engine.metrics.FinancialMetricsService;
import org.springframework.stereotype.Service;

@Service
public class CreditRiskEvaluationService {

    private final FinancialMetricsService financialMetricsService;
    private final CreditScoringService creditScoringService;
    private final RiskDecisionService riskDecisionService;

    public CreditRiskEvaluationService(
            FinancialMetricsService financialMetricsService,
            CreditScoringService creditScoringService,
            RiskDecisionService riskDecisionService
    ) {
        this.financialMetricsService = financialMetricsService;
        this.creditScoringService = creditScoringService;
        this.riskDecisionService = riskDecisionService;
    }

    /**
     * Orchestrates full credit risk evaluation.
     */
    public CreditRiskResponseDTO evaluate(CreditRiskRequestDTO request) {

        // 1️⃣ Map DTO → Entity (pure data mapping)
        UserFinancialProfile profile = mapToEntity(request);

        // 2️⃣ Compute derived metrics
        FinancialMetrics metrics =
                financialMetricsService.computeMetrics(profile);

        // 3️⃣ Apply scoring rules
        ScoringResult scoringResult =
                creditScoringService.calculateScore(profile, metrics);

        int finalScore = scoringResult.getFinalScore();

        // 4️⃣ Determine risk & decision
        RiskLevel riskLevel =
                riskDecisionService.determineRiskLevel(finalScore);

        Decision decision =
                riskDecisionService.determineDecision(riskLevel);

        // 5️⃣ Build final response
        return new CreditRiskResponseDTO(
                finalScore,
                riskLevel,
                decision,
                scoringResult.getReasons()
        );
    }

    /**
     * DTO → Entity mapping (no logic)
     */
    private UserFinancialProfile mapToEntity(CreditRiskRequestDTO request) {

        UserFinancialProfile profile = new UserFinancialProfile();

        profile.setMonthlyIncome(request.getMonthlyIncome());
        profile.setMonthlyExpenses(request.getMonthlyExpenses());
        profile.setTotalMonthlyEmis(request.getTotalMonthlyEmis());
        profile.setPastLoanDefaults(request.getPastLoanDefaults());
        profile.setCreditHistoryLengthMonths(request.getCreditHistoryLengthMonths());
        profile.setEmploymentType(request.getEmploymentType());
        profile.setAge(request.getAge());
        profile.setRequestedLoanAmount(request.getRequestedLoanAmount());

        return profile;
    }
}
