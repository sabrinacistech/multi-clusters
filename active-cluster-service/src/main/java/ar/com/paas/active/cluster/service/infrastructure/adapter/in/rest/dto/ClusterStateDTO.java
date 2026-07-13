package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto;

import java.time.Instant;
import java.util.Map;

/**
 * DTO completo del estado de cluster (envelope data para inicial/create/update).
 */
public record ClusterStateDTO(
        String id,
        String application,
        String project,
        String environment,
        String activeCluster,
        Boolean active,
        Integer pollingIntervalSeconds,
        String updatedBy,
        Instant updatedAt,
        String reason,
        Map<String, Object> metadata) {
}
