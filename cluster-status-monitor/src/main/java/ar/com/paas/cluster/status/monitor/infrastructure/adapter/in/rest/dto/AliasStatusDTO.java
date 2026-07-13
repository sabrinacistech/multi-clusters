package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto;

import java.time.Instant;

/**
 * DTO de salida para estado de alias.
 */
public record AliasStatusDTO(
        String id,
        String alias,
        String app,
        String cluster,
        String target,
        Boolean isActive,
        Instant updatedAt) {
}
