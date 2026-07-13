package ar.com.paas.active.cluster.service.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Modelo de dominio inmutable que representa el estado de un cluster.
 */
@Value
@Builder
public class ClusterState {

    String id;
    String application;
    String project;
    String environment;
    String activeCluster;
    Boolean active;
    @Builder.Default
    Integer pollingIntervalSeconds = 30;
    String updatedBy;
    Instant updatedAt;
    String reason;
    Map<String, Object> metadata;

    /**
     * Clave logica del estado: application:project:environment.
     */
    public String logicalKey() {
        return application + ":" + project + ":" + environment;
    }

    /**
     * Valida las invariantes del dominio y construye una instancia validada.
     */
    public static ClusterState of(ClusterStateBuilder builder) {
        ClusterState state = builder.build();
        state.validateInvariants();
        return state;
    }

    /**
     * Verifica que los campos obligatorios no sean nulos.
     */
    public void validateInvariants() {
        Objects.requireNonNull(application, "application no puede ser nulo");
        Objects.requireNonNull(project, "project no puede ser nulo");
        Objects.requireNonNull(environment, "environment no puede ser nulo");
        Objects.requireNonNull(activeCluster, "activeCluster no puede ser nulo");
        Objects.requireNonNull(active, "active no puede ser nulo");
        Objects.requireNonNull(updatedBy, "updatedBy no puede ser nulo");
        Objects.requireNonNull(updatedAt, "updatedAt no puede ser nulo");
    }
}
