package ar.com.paas.cluster.status.monitor.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.Instant;

/**
 * Estado de un clúster para una app determinada.
 * Modelo de dominio inmutable.
 * <p>
 * Clave lógica CSM: {@code codigo:app:cluster}.
 */
@Value
@With
public class ClusterStatus {

    public static final String UPDATED_BY_MANUAL = "MANUAL";
    public static final String UPDATED_BY_SCHEDULE = "SCHEDULE";

    String id;
    String codigo;
    String app;
    String cluster;
    Boolean isActive;
    /** Origen de la última actualización: MANUAL | SCHEDULE. */
    String updatedBy;
    Instant updatedAt;
    Instant lastManualAt;
    Long version;

    /**
     * Constructor reducido usado por los tests del SDD.
     */
    public ClusterStatus(String id, String codigo, String app, String cluster, Boolean isActive) {
        this(id, codigo, app, cluster, isActive, UPDATED_BY_SCHEDULE, Instant.now(), null, null);
    }

    @Builder
    public ClusterStatus(String id, String codigo, String app, String cluster, Boolean isActive,
                         String updatedBy, Instant updatedAt, Instant lastManualAt, Long version) {
        this.id = id;
        this.codigo = codigo;
        this.app = app;
        this.cluster = cluster;
        this.isActive = isActive;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.lastManualAt = lastManualAt;
        this.version = version;
    }

    /**
     * Clave lógica / de cache CSM: {@code codigo:app:cluster}.
     */
    public String cacheKey() {
        return codigo + ":" + app + ":" + cluster;
    }

    /**
     * Fabrica un estado a partir de un objetivo de clúster y el resultado del sondeo.
     */
    public static ClusterStatus from(ClusterTarget target, HealthResult health) {
        return ClusterStatus.builder()
                .codigo(target.getCluster())
                .app(target.getApp())
                .cluster(target.getCluster())
                .isActive(health != null && health.isUp())
                .updatedBy(UPDATED_BY_SCHEDULE)
                .updatedAt(Instant.now())
                .build();
    }
}
