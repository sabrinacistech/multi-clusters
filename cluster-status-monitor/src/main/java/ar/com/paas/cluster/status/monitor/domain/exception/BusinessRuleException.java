package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Violación de regla de negocio (CSM-022).
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException() {
        super(ErrorCode.CSM_022, "Regla de negocio violada");
    }

    public BusinessRuleException(String message) {
        super(ErrorCode.CSM_022, message);
    }
}
