package ar.com.paas.cluster.status.monitor.domain.exception;

import lombok.Getter;

/**
 * Excepción base del dominio. Lleva un código CSM.
 */
@Getter
public class DomainException extends RuntimeException {

    private final String code;

    public DomainException(String code) {
        super(code);
        this.code = code;
    }

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public DomainException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
