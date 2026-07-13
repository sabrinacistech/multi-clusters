package ar.com.paas.active.cluster.service.application.port.out;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;

import java.util.Optional;

/**
 * Puerto de salida hacia el repositorio de estados de cluster.
 */
public interface ClusterStateRepositoryPort {

    Optional<ClusterState> findActiveByLogicalKey(String application, String project, String environment);

    ClusterState save(ClusterState state);

    Optional<ClusterState> findById(String id);
}
