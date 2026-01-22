package com.Raman.credit_risk_engine.service;

import com.Raman.credit_risk_engine.rule.*;

import java.util.List;

public class RuleRegistry {

    public static List<CreditRule> orderedRules() {
        return List.of(
                new IncomeStabilityRule(),
                new DtiRule(),
                new DefaultHistoryRule(),
                new CreditHistoryRule(),
                new DisposableIncomeRule()
        );
    }
}
