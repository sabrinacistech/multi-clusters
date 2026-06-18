package com.sabrinacistech.multiclusters.exception;

import com.sabrinacistech.multiclusters.enums.ClusterExceptionMessage;

/**
 * Excepción de dominio que indica que no existe un clúster para el alias configurado.
 *
 * <p><b>Capa:</b> domain.exception.</p>
 * <p><b>Cuándo ocurre:</b> cuando el caso de uso intenta obtener el clúster configurado y no hay registro.</p>
 * <p><b>Efecto esperado:</b> el caso de uso debería abortar y/o limpiar el store en memoria para evitar inconsistencia.</p>
 */

public class ClusterNotFoundException extends RuntimeException {
    public ClusterNotFoundException(String message) {
        super(message);
    }

    public ClusterNotFoundException(ClusterExceptionMessage exceptionMessage, String context) {
        super(exceptionMessage.getMessage() + ": " + context);
    }
}