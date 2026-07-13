package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de entrada para ABM de estado de clúster.
 */
public record ClusterStatusInput(
        @NotBlank(message = "codigo es obligatorio") String codigo,
        @NotBlank(message = "app es obligatorio") String app,
        @NotBlank(message = "cluster es obligatorio") String cluster,
        @NotNull(message = "isActive es obligatorio") Boolean isActive,
        String updatedBy) {
}
