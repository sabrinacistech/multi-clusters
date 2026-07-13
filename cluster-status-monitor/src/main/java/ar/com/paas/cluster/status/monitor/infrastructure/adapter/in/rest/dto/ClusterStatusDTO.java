package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto;

import java.time.Instant;

/**
 * DTO de salida para estado de clúster.
 */
public record ClusterStatusDTO(
        String id,
        String codigo,
        String app,
        String cluster,
        Boolean isActive,
        String updatedBy,
        Instant updatedAt,
        Instant lastManualAt,
        Long version) {
}
