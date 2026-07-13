package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Error de persistencia / base de datos (CSM-007).
 */
public class PersistenceException extends DomainException {

    public PersistenceException(String message) {
        super(ErrorCode.CSM_007, message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(ErrorCode.CSM_007, message, cause);
    }
}
