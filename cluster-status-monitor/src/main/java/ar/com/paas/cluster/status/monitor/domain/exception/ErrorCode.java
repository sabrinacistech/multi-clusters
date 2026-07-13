package ar.com.paas.cluster.status.monitor.domain.exception;

/**
 * Códigos de error CSM del dominio.
 */
public final class ErrorCode {

    private ErrorCode() {
    }

    /** Validación de entrada inválida. */
    public static final String CSM_001 = "CSM-001";
    /** Recurso no encontrado. */
    public static final String CSM_004 = "CSM-004";
    /** Conflicto: clave lógica duplicada. */
    public static final String CSM_009 = "CSM-009";
    /** Regla de negocio violada. */
    public static final String CSM_022 = "CSM-022";
    /** Persistencia / base de datos no disponible. */
    public static final String CSM_007 = "CSM-007";
    /** Warning de sondeo (probe) aislado por clúster. */
    public static final String CSM_201 = "CSM-201";
    /** Error interno / error de ciclo. */
    public static final String CSM_500 = "CSM-500";
}
