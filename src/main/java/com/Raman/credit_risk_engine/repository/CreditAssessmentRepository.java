package com.Raman.credit_risk_engine.repository;

import com.Raman.credit_risk_engine.entity.CreditAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditAssessmentRepository extends JpaRepository<CreditAssessment, Long> {
    // New query for User Isolation
    List<CreditAssessment> findByUserIdOrderByCreatedAtDesc(Long userId);
}