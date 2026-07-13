package ar.com.paas.cluster.status.monitor.application.port.out;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;

import java.util.List;

/**
 * Catálogo de clústeres registrados a sondear.
 */
public interface ClusterCatalog {

    List<ClusterTarget> registered();
}
