package ar.com.paas.cluster.status.monitor.application.port.in;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;

import java.util.List;

/**
 * Caso de uso ABM (alta/baja/modificación) de estados de clúster.
 */
public interface ManageClusterStatusUseCase {

    List<ClusterStatus> list();

    ClusterStatus get(String id);

    ClusterStatus create(ClusterStatus s);

    ClusterStatus update(String id, ClusterStatus s);

    void delete(String id);
}
