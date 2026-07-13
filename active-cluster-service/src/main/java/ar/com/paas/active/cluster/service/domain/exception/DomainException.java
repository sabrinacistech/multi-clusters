package ar.com.paas.active.cluster.service.domain.exception;

import lombok.Getter;

/**
 * Excepcion base del dominio con un codigo de error asociado.
 */
@Getter
public class DomainException extends RuntimeException {

    private final String code;

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public DomainException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
