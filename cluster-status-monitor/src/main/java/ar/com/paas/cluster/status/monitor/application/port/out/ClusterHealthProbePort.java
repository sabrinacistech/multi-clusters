package ar.com.paas.cluster.status.monitor.application.port.out;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;
import ar.com.paas.cluster.status.monitor.domain.model.HealthResult;

/**
 * Puerto de salida para sondear la salud de un clúster.
 */
public interface ClusterHealthProbePort {

    /**
     * Sondea el objetivo indicado.
     *
     * @throws ar.com.paas.cluster.status.monitor.domain.exception.ProbeException si se agotan los reintentos.
     */
    HealthResult check(ClusterTarget target);
}
