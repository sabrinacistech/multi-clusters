package ar.com.paas.active.cluster.service.domain.exception;

/**
 * Se lanza cuando los parametros de entrada son invalidos (ACS-001).
 */
public class InvalidParametersException extends DomainException {

    public InvalidParametersException(String message) {
        super(ErrorCode.INVALID_PARAMETERS.code(), message);
    }
}
