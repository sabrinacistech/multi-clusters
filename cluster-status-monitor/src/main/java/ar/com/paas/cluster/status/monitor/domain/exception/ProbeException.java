package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Error de sondeo (probe) contra un clúster (CSM-201).
 */
public class ProbeException extends DomainException {

    public ProbeException(String message) {
        super(ErrorCode.CSM_201, message);
    }

    public ProbeException(String message, Throwable cause) {
        super(ErrorCode.CSM_201, message, cause);
    }
}
