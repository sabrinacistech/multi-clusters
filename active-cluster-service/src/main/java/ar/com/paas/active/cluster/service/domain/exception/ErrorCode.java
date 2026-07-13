package ar.com.paas.active.cluster.service.domain.exception;

/**
 * Codigos de error del dominio ACS.
 */
public enum ErrorCode {

    INVALID_PARAMETERS("ACS-001"),
    CLUSTER_STATE_NOT_FOUND("ACS-004"),
    SERVICE_UNAVAILABLE("ACS-007"),
    INTERNAL_ERROR("ACS-500");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
