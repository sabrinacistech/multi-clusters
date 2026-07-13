package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto;

/**
 * DTO de datos para la respuesta de get-cluster-status.
 */
public record ClusterStatusDTO(boolean active, int pollingIntervalSeconds) {
}
