package com.Raman.credit_risk_engine.repository;

import com.Raman.credit_risk_engine.entity.AssessmentAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentAuditRepository extends JpaRepository<AssessmentAudit, Long> {
    // 🔍 Efficiently find audits for a specific assessment ID
    List<AssessmentAudit> findByCreditAssessmentId(Long assessmentId);
}