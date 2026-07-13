package ar.com.paas.cluster.status.monitor.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.Instant;

/**
 * Estado de un alias que apunta a un clúster/target.
 * Modelo de dominio inmutable.
 */
@Value
@Builder
@With
public class AliasStatus {

    String id;
    /** Nombre del alias (clave lógica única). */
    String alias;
    /** Aplicación asociada. */
    String app;
    /** Clúster/target destino del alias. */
    String cluster;
    String target;
    Boolean isActive;
    Instant updatedAt;
}
