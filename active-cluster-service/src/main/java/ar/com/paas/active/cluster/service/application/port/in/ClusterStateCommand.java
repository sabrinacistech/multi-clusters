package ar.com.paas.active.cluster.service.application.port.in;

import java.util.Map;

/**
 * Comando de dominio para crear/actualizar/registrar un estado de cluster.
 */
public record ClusterStateCommand(
        String id,
        String application,
        String project,
        String environment,
        String activeCluster,
        Boolean active,
        Integer pollingIntervalSeconds,
        String updatedBy,
        String reason,
        Map<String, Object> metadata) {
}
