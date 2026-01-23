package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.decision.*;
import com.Raman.credit_risk_engine.dto.*;
import com.Raman.credit_risk_engine.entity.*;
import com.Raman.credit_risk_engine.metrics.*;
import com.Raman.credit_risk_engine.repository.*;
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
    private final UserFinancialProfileRepository userFinancialProfileRepository;
    private final UserRepository userRepository;

    public CreditRiskEvaluationService(
            FinancialMetricsService financialMetricsService,
            CreditScoringService creditScoringService,
            RiskDecisionService riskDecisionService,
            CreditAssessmentRepository creditAssessmentRepository,
            AssessmentAuditRepository assessmentAuditRepository,
            UserFinancialProfileRepository userFinancialProfileRepository,
            UserRepository userRepository
    ) {
        this.financialMetricsService = financialMetricsService;
        this.creditScoringService = creditScoringService;
        this.riskDecisionService = riskDecisionService;
        this.creditAssessmentRepository = creditAssessmentRepository;
        this.assessmentAuditRepository = assessmentAuditRepository;
        this.userFinancialProfileRepository = userFinancialProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreditRiskResponseDTO evaluate(CreditRiskRequestDTO request, Long userId) {
        UserFinancialProfile profile = mapToEntity(request);
        userFinancialProfileRepository.save(profile);

        FinancialMetrics metrics = financialMetricsService.computeMetrics(profile);
        ScoringResult scoringResult = creditScoringService.calculateScore(profile, metrics);

        CreditAssessment assessment = new CreditAssessment();
        // Convert BigDecimal/Enum to Double/String for storage
        assessment.setMonthlyIncome(request.getMonthlyIncome().doubleValue());
        assessment.setMonthlyExpenses(request.getMonthlyExpenses().doubleValue());
        assessment.setTotalMonthlyEmis(request.getTotalMonthlyEmis().doubleValue());
        assessment.setRequestedLoanAmount(request.getRequestedLoanAmount().doubleValue());
        assessment.setAge(request.getAge());
        assessment.setPastLoanDefaults(request.getPastLoanDefaults());
        assessment.setCreditHistoryLengthMonths(request.getCreditHistoryLengthMonths());
        assessment.setEmploymentType(request.getEmploymentType().name());

        assessment.setCreditScore(scoringResult.getFinalScore());
        assessment.setRiskLevel(riskDecisionService.determineRiskLevel(scoringResult.getFinalScore()));
        assessment.setDecision(riskDecisionService.determineDecision(assessment.getRiskLevel()));

        userRepository.findById(userId).ifPresent(assessment::setUser);
        CreditAssessment savedAssessment = creditAssessmentRepository.save(assessment);

        saveAudits(savedAssessment, scoringResult.getRuleResults());

        return new CreditRiskResponseDTO(assessment.getCreditScore(), assessment.getRiskLevel(), assessment.getDecision(),
                scoringResult.getRuleResults().stream().map(RuleResult::getReason).collect(Collectors.toList()));
    }

    @Transactional
    public CreditAssessment updateAndReEvaluate(Long id, CreditAssessment updatedData) {
        return creditAssessmentRepository.findById(id).map(existing -> {
            existing.setMonthlyIncome(updatedData.getMonthlyIncome());
            existing.setMonthlyExpenses(updatedData.getMonthlyExpenses());
            existing.setTotalMonthlyEmis(updatedData.getTotalMonthlyEmis());
            existing.setPastLoanDefaults(updatedData.getPastLoanDefaults());

            UserFinancialProfile profile = new UserFinancialProfile();
            profile.setMonthlyIncome(java.math.BigDecimal.valueOf(existing.getMonthlyIncome()));
            profile.setMonthlyExpenses(java.math.BigDecimal.valueOf(existing.getMonthlyExpenses()));
            profile.setTotalMonthlyEmis(java.math.BigDecimal.valueOf(existing.getTotalMonthlyEmis()));
            profile.setPastLoanDefaults(existing.getPastLoanDefaults());
            profile.setEmploymentType(EmploymentType.valueOf(existing.getEmploymentType()));
            profile.setAge(existing.getAge());
            profile.setCreditHistoryLengthMonths(existing.getCreditHistoryLengthMonths());
            profile.setRequestedLoanAmount(java.math.BigDecimal.valueOf(existing.getRequestedLoanAmount()));

            FinancialMetrics metrics = financialMetricsService.computeMetrics(profile);
            ScoringResult scoringResult = creditScoringService.calculateScore(profile, metrics);

            existing.setCreditScore(scoringResult.getFinalScore());
            existing.setRiskLevel(riskDecisionService.determineRiskLevel(scoringResult.getFinalScore()));
            existing.setDecision(riskDecisionService.determineDecision(existing.getRiskLevel()));

            existing.getAudits().clear();
            saveAudits(existing, scoringResult.getRuleResults());

            return creditAssessmentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Assessment not found"));
    }

    private void saveAudits(CreditAssessment assessment, List<RuleResult> results) {
        for (RuleResult res : results) {
            AssessmentAudit audit = new AssessmentAudit();
            audit.setCreditAssessment(assessment);
            audit.setRuleName(res.getRuleName());
            audit.setScoreImpact(res.getScoreImpact());
            audit.setReason(res.getReason());
            assessmentAuditRepository.save(audit);
        }
    }

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