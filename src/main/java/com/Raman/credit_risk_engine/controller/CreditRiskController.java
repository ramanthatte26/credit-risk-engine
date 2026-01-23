package com.Raman.credit_risk_engine.controller;

import com.Raman.credit_risk_engine.dto.CreditRiskRequestDTO;
import com.Raman.credit_risk_engine.dto.CreditRiskResponseDTO;
import com.Raman.credit_risk_engine.entity.AssessmentAudit;
import com.Raman.credit_risk_engine.entity.CreditAssessment;
import com.Raman.credit_risk_engine.repository.AssessmentAuditRepository;
import com.Raman.credit_risk_engine.repository.CreditAssessmentRepository;
import com.Raman.credit_risk_engine.service.CreditRiskEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-risk")
public class CreditRiskController {

    private final CreditRiskEvaluationService evaluationService;
    private final CreditAssessmentRepository assessmentRepository;
    private final AssessmentAuditRepository auditRepository;

    public CreditRiskController(
            CreditRiskEvaluationService evaluationService,
            CreditAssessmentRepository assessmentRepository,
            AssessmentAuditRepository auditRepository
    ) {
        this.evaluationService = evaluationService;
        this.assessmentRepository = assessmentRepository;
        this.auditRepository = auditRepository;
    }

    @PostMapping("/evaluate/{userId}")
    public ResponseEntity<CreditRiskResponseDTO> evaluateCreditRisk(
            @Valid @RequestBody CreditRiskRequestDTO request,
            @PathVariable Long userId
    ) {
        CreditRiskResponseDTO response = evaluationService.evaluate(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<CreditAssessment>> getAssessmentHistory(@PathVariable Long userId) {
        List<CreditAssessment> history = assessmentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/{id}/audit")
    public ResponseEntity<List<AssessmentAudit>> getAssessmentAudit(@PathVariable Long id) {
        List<AssessmentAudit> audits = auditRepository.findByCreditAssessmentId(id);
        return ResponseEntity.ok(audits);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditAssessment> updateAssessment(
            @PathVariable Long id,
            @RequestBody CreditAssessment updatedData
    ) {
        // Calls the service method that re-runs the scoring engine
        CreditAssessment updated = evaluationService.updateAndReEvaluate(id, updatedData);
        return ResponseEntity.ok(updated);
    }
}