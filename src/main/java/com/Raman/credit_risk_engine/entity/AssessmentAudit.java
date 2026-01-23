package com.Raman.credit_risk_engine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // Import this
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "assessment_audit")
public class AssessmentAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_assessment_id", nullable = false)
    @JsonIgnore // 🚨 CRITICAL: Prevents Infinite Recursion (StackOverflow)
    private CreditAssessment creditAssessment;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "score_impact", nullable = false)
    private Integer scoreImpact;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    // Standard Getters & Setters
    public Long getId() { return id; }
    public CreditAssessment getCreditAssessment() { return creditAssessment; }
    public void setCreditAssessment(CreditAssessment creditAssessment) { this.creditAssessment = creditAssessment; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public Integer getScoreImpact() { return scoreImpact; }
    public void setScoreImpact(Integer scoreImpact) { this.scoreImpact = scoreImpact; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Instant getCreatedAt() { return createdAt; }
}