package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de entrada para ABM de estado de alias.
 */
public record AliasStatusInput(
        @NotBlank(message = "alias es obligatorio") String alias,
        @NotBlank(message = "app es obligatorio") String app,
        @NotBlank(message = "cluster es obligatorio") String cluster,
        String target,
        @NotNull(message = "isActive es obligatorio") Boolean isActive) {
}
