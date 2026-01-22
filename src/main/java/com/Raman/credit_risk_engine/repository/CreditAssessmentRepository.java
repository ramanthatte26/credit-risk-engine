package com.Raman.credit_risk_engine.repository;

import com.Raman.credit_risk_engine.entity.CreditAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditAssessmentRepository
        extends JpaRepository<CreditAssessment, Long> {
}
