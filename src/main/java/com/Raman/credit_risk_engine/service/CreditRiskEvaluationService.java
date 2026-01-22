package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.decision.Decision;
import com.Raman.credit_risk_engine.decision.RiskDecisionService;
import com.Raman.credit_risk_engine.decision.RiskLevel;
import com.Raman.credit_risk_engine.dto.CreditRiskRequestDTO;
import com.Raman.credit_risk_engine.dto.CreditRiskResponseDTO;
import com.Raman.credit_risk_engine.entity.AssessmentAudit;
import com.Raman.credit_risk_engine.entity.CreditAssessment;
import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import com.Raman.credit_risk_engine.metrics.FinancialMetrics;
import com.Raman.credit_risk_engine.metrics.FinancialMetricsService;
import com.Raman.credit_risk_engine.repository.AssessmentAuditRepository;
import com.Raman.credit_risk_engine.repository.CreditAssessmentRepository;
import com.Raman.credit_risk_engine.rule.RuleResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditRiskEvaluationService {

    private final FinancialMetricsService financialMetricsService;
    private final CreditScoringService creditScoringService;
    private final RiskDecisionService riskDecisionService;
    private final CreditAssessmentRepository creditAssessmentRepository;
    private final AssessmentAuditRepository assessmentAuditRepository;

    public CreditRiskEvaluationService(
            FinancialMetricsService financialMetricsService,
            CreditScoringService creditScoringService,
            RiskDecisionService riskDecisionService,
            CreditAssessmentRepository creditAssessmentRepository,
            AssessmentAuditRepository assessmentAuditRepository
    ) {
        this.financialMetricsService = financialMetricsService;
        this.creditScoringService = creditScoringService;
        this.riskDecisionService = riskDecisionService;
        this.creditAssessmentRepository = creditAssessmentRepository;
        this.assessmentAuditRepository = assessmentAuditRepository;
    }

    /**
     * Orchestrates full credit risk evaluation.
     * Atomic: decision + audit are persisted together.
     */
    @Transactional
    public CreditRiskResponseDTO evaluate(CreditRiskRequestDTO request) {

        // 1️⃣ Map DTO → Entity
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

        // 5️⃣ Persist CreditAssessment
        CreditAssessment assessment = new CreditAssessment();
        assessment.setCreditScore(finalScore);
        assessment.setRiskLevel(riskLevel);
        assessment.setDecision(decision);

        CreditAssessment savedAssessment =
                creditAssessmentRepository.save(assessment);

        // 6️⃣ Persist AssessmentAudit entries
        for (RuleResult ruleResult : scoringResult.getRuleResults()) {
            AssessmentAudit audit = new AssessmentAudit();
            audit.setCreditAssessment(savedAssessment);
            audit.setRuleName(ruleResult.getRuleName());
            audit.setScoreImpact(ruleResult.getScoreImpact());
            audit.setReason(ruleResult.getReason());

            assessmentAuditRepository.save(audit);
        }

        // 7️⃣ Extract reasons for API response
        List<String> reasons = scoringResult.getRuleResults()
                .stream()
                .map(RuleResult::getReason)
                .collect(Collectors.toList());

        // 8️⃣ Return API response
        return new CreditRiskResponseDTO(
                finalScore,
                riskLevel,
                decision,
                reasons
        );
    }

    /**
     * DTO → Entity mapping (pure data mapping)
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
