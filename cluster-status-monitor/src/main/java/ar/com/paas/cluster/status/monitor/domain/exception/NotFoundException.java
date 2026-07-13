package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Recurso no encontrado (CSM-004).
 */
public class NotFoundException extends DomainException {

    public NotFoundException() {
        super(ErrorCode.CSM_004, "Recurso no encontrado");
    }

    public NotFoundException(String message) {
        super(ErrorCode.CSM_004, message);
    }
}
