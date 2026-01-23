package com.Raman.credit_risk_engine.entity;

import com.Raman.credit_risk_engine.decision.Decision;
import com.Raman.credit_risk_engine.decision.RiskLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "credit_assessment")
public class CreditAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Financial Inputs
    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double totalMonthlyEmis;
    private Double requestedLoanAmount;
    private Integer age;
    private Integer pastLoanDefaults;
    private Integer creditHistoryLengthMonths;
    private String employmentType;

    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20)
    private Decision decision;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "creditAssessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentAudit> audits;

    @PrePersist
    protected void onCreate() { this.createdAt = Instant.now(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public Double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public Double getTotalMonthlyEmis() { return totalMonthlyEmis; }
    public void setTotalMonthlyEmis(Double totalMonthlyEmis) { this.totalMonthlyEmis = totalMonthlyEmis; }
    public Double getRequestedLoanAmount() { return requestedLoanAmount; }
    public void setRequestedLoanAmount(Double requestedLoanAmount) { this.requestedLoanAmount = requestedLoanAmount; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Integer getPastLoanDefaults() { return pastLoanDefaults; }
    public void setPastLoanDefaults(Integer pastLoanDefaults) { this.pastLoanDefaults = pastLoanDefaults; }
    public Integer getCreditHistoryLengthMonths() { return creditHistoryLengthMonths; }
    public void setCreditHistoryLengthMonths(Integer creditHistoryLengthMonths) { this.creditHistoryLengthMonths = creditHistoryLengthMonths; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public Decision getDecision() { return decision; }
    public void setDecision(Decision decision) { this.decision = decision; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<AssessmentAudit> getAudits() { return audits; }
    public void setAudits(List<AssessmentAudit> audits) { this.audits = audits; }
}