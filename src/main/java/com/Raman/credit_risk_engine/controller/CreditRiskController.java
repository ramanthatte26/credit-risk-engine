package com.Raman.credit_risk_engine.controller;

import com.Raman.credit_risk_engine.dto.CreditRiskRequestDTO;
import com.Raman.credit_risk_engine.dto.CreditRiskResponseDTO;
import com.Raman.credit_risk_engine.service.CreditRiskEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit-risk")
public class CreditRiskController {

    private final CreditRiskEvaluationService evaluationService;

    public CreditRiskController(CreditRiskEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * Evaluates credit risk for a given financial profile.
     */
    @PostMapping("/evaluate")
    public ResponseEntity<CreditRiskResponseDTO> evaluateCreditRisk(
            @Valid @RequestBody CreditRiskRequestDTO request
    ) {
        CreditRiskResponseDTO response =
                evaluationService.evaluate(request);

        return ResponseEntity.ok(response);
    }
}
