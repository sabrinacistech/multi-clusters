package ar.com.paas.active.cluster.service.application.port.in;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;

/**
 * Puerto de entrada para crear un estado de cluster (compatibilidad).
 */
public interface CreateClusterStateUseCase {

    ClusterState create(ClusterStateCommand command);
}
