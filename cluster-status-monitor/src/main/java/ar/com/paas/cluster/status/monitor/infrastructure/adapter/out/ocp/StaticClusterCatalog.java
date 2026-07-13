package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.ocp;

import ar.com.paas.cluster.status.monitor.application.port.out.ClusterCatalog;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;
import ar.com.paas.cluster.status.monitor.infrastructure.config.CsmProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Catálogo estático de clústeres leído desde la configuración {@code csm.clusters}.
 */
@Component
@RequiredArgsConstructor
public class StaticClusterCatalog implements ClusterCatalog {

    private final CsmProperties properties;

    @Override
    public List<ClusterTarget> registered() {
        return properties.getClusters().stream()
                .map(c -> new ClusterTarget(c.getApp(), c.getCluster(), c.getHealthUrl()))
                .toList();
    }
}
