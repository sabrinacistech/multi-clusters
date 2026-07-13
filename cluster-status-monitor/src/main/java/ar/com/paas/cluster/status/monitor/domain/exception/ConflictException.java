package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Conflicto: clave lógica duplicada (CSM-009).
 */
public class ConflictException extends DomainException {

    public ConflictException() {
        super(ErrorCode.CSM_009, "Conflicto: clave logica duplicada");
    }

    public ConflictException(String message) {
        super(ErrorCode.CSM_009, message);
    }
}
