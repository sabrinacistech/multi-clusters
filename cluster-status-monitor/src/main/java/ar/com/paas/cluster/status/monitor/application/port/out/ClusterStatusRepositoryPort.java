package ar.com.paas.cluster.status.monitor.application.port.out;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de estados de clúster.
 */
public interface ClusterStatusRepositoryPort {

    /**
     * Inserta o actualiza (upsert) por clave lógica {@code codigo:app:cluster}.
     */
    ClusterStatus upsert(ClusterStatus status);

    List<ClusterStatus> findAll();

    Optional<ClusterStatus> findById(String id);

    void deleteById(String id);

    ClusterStatus save(ClusterStatus status);
}
