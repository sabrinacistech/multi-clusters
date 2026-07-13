package ar.com.paas.cluster.status.monitor.application.service;

import ar.com.paas.cluster.status.monitor.application.port.in.PollClusterStatusUseCase;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterCatalog;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterHealthProbePort;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.application.port.out.StatusCachePort;
import ar.com.paas.cluster.status.monitor.domain.exception.PersistenceException;
import ar.com.paas.cluster.status.monitor.domain.exception.ProbeException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;
import ar.com.paas.cluster.status.monitor.domain.model.HealthResult;
import ar.com.paas.cluster.status.monitor.domain.model.PollSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que sondea todos los clústeres registrados, persiste su estado y
 * refresca el cache. Aísla los fallos por clúster para no abortar el ciclo.
 */
@Slf4j
@Service
public class ClusterStatusMonitorService implements PollClusterStatusUseCase {

    private final ClusterCatalog clusterCatalog;
    private final ClusterHealthProbePort healthProbePort;
    private final ClusterStatusRepositoryPort repositoryPort;
    private final StatusCachePort cachePort;
    private final MeterRegistry meterRegistry;

    public ClusterStatusMonitorService(ClusterCatalog clusterCatalog,
                                       ClusterHealthProbePort healthProbePort,
                                       ClusterStatusRepositoryPort repositoryPort,
                                       StatusCachePort cachePort,
                                       MeterRegistry meterRegistry) {
        this.clusterCatalog = clusterCatalog;
        this.healthProbePort = healthProbePort;
        this.repositoryPort = repositoryPort;
        this.cachePort = cachePort;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public PollSummary pollAll() {
        List<ClusterTarget> targets = clusterCatalog.registered();
        int ok = 0;
        int failed = 0;

        for (ClusterTarget target : targets) {
            Timer.Sample sample = Timer.start(meterRegistry);
            try {
                HealthResult health = healthProbePort.check(target);
                ClusterStatus status = ClusterStatus.from(target, health);
                repositoryPort.upsert(status);
                cachePort.refresh(status.cacheKey(), status);
                ok++;
            } catch (ProbeException | PersistenceException ex) {
                // CSM-201: se aísla el fallo por clúster y se continúa con el resto.
                log.warn("CSM-201 fallo al sondear/persistir cluster '{}': {}",
                        target.name(), ex.getMessage());
                failed++;
            } finally {
                sample.stop(meterRegistry.timer("csm_probe_latency_seconds",
                        "cluster", target.name()));
            }
        }

        log.info("Ciclo de sondeo finalizado: ok={}, failed={}", ok, failed);
        return new PollSummary(ok, failed);
    }
}
