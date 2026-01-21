package com.Raman.credit_risk_engine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "user_financial_profile")
public class UserFinancialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @DecimalMin(value = "0.01",inclusive = true)
    @Column(name="monthly_income",nullable = false)
    private BigDecimal monthlyIncome;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "monthly_expenses", nullable = false)
    private BigDecimal monthlyExpenses;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_monthly_emis", nullable = false)
    private BigDecimal totalMonthlyEmis;

    @NotNull
    @Min(0)
    @Column(name = "past_loan_defaults", nullable = false)
    private Integer pastLoanDefaults;

    @NotNull
    @Min(0)
    @Column(name = "credit_history_length_months", nullable = false)
    private Integer creditHistoryLengthMonths;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 20)
    private EmploymentType employmentType;

    @NotNull
    @Min(18)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    @Column(name = "requested_loan_amount", nullable = false)
    private BigDecimal requestedLoanAmount;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public BigDecimal getTotalMonthlyEmis() {
        return totalMonthlyEmis;
    }

    public void setTotalMonthlyEmis(BigDecimal totalMonthlyEmis) {
        this.totalMonthlyEmis = totalMonthlyEmis;
    }

    public Integer getPastLoanDefaults() {
        return pastLoanDefaults;
    }

    public void setPastLoanDefaults(Integer pastLoanDefaults) {
        this.pastLoanDefaults = pastLoanDefaults;
    }

    public Integer getCreditHistoryLengthMonths() {
        return creditHistoryLengthMonths;
    }

    public void setCreditHistoryLengthMonths(Integer creditHistoryLengthMonths) {
        this.creditHistoryLengthMonths = creditHistoryLengthMonths;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getRequestedLoanAmount() {
        return requestedLoanAmount;
    }

    public void setRequestedLoanAmount(BigDecimal requestedLoanAmount) {
        this.requestedLoanAmount = requestedLoanAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
