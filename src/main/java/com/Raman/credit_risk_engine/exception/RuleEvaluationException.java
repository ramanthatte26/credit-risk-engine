package com.Raman.credit_risk_engine.exception;

/**
 * Represents business-level failures during credit underwriting.
 *
 * This exception is thrown when:
 * - A rule cannot be evaluated
 * - Financial metrics are inconsistent or invalid
 * - Business assumptions are violated
 *
 * This is NOT a system crash exception.
 */
public class RuleEvaluationException extends RuntimeException {

    public RuleEvaluationException(String message) {
        super(message);
    }

    public RuleEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
