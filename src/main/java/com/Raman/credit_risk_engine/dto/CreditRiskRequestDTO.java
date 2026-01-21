package com.Raman.credit_risk_engine.dto;

import com.Raman.credit_risk_engine.entity.EmploymentType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreditRiskRequestDTO {

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal monthlyIncome;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal monthlyExpenses;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal totalMonthlyEmis;

    @NotNull
    @Min(0)
    private Integer pastLoanDefaults;

    @NotNull
    @Min(0)
    private Integer creditHistoryLengthMonths;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    @Min(18)
    private Integer age;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal requestedLoanAmount;

    // Getters and Setters only

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
}
