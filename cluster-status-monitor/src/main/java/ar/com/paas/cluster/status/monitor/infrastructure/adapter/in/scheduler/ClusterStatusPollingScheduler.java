package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.scheduler;

import ar.com.paas.cluster.status.monitor.application.port.in.PollClusterStatusUseCase;
import ar.com.paas.cluster.status.monitor.domain.model.PollSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Disparador programado del ciclo de sondeo. Sólo activo si csm.scheduler.enabled=true.
 * En OpenShift debe correr con una única réplica activa (ver ShedLock / lease en el deployment).
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "csm.scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ClusterStatusPollingScheduler {

    private final PollClusterStatusUseCase pollUseCase;
    private final MeterRegistry meterRegistry;

    @Scheduled(
            fixedDelayString = "${csm.scheduler.fixed-delay:30000}",
            initialDelayString = "${csm.scheduler.initial-delay:10000}")
    public void pollAllClusters() {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            PollSummary summary = pollUseCase.pollAll();
            meterRegistry.counter("csm_poll_clusters_total", "result", "success")
                    .increment(summary.getSucceeded());
            meterRegistry.counter("csm_poll_clusters_total", "result", "failed")
                    .increment(summary.getFailed());
            log.info("Ciclo de sondeo programado OK: {}", summary);
        } catch (Exception ex) {
            meterRegistry.counter("csm_poll_cycle_errors_total").increment();
            log.error("CSM-500 error en el ciclo de sondeo programado: {}", ex.getMessage(), ex);
        } finally {
            sample.stop(meterRegistry.timer("csm_poll_cycle_duration_seconds"));
        }
    }
}
