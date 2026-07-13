package ar.com.paas.active.cluster.service.application.port.in;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;

/**
 * Puerto de entrada para el registro inicial del estado (compatibilidad).
 */
public interface RegisterInitialUseCase {

    ClusterState registerInitial(ClusterStateCommand command);
}
