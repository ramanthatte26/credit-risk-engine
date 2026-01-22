package com.Raman.credit_risk_engine.decision;

/**
 * Represents the final business action taken after risk evaluation.
 *
 * This is a closed set to ensure deterministic and auditable decisions.
 */
public enum Decision {

    APPROVE,
    REVIEW,
    REJECT

}
