package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.ocp;

import ar.com.paas.cluster.status.monitor.application.port.out.ClusterHealthProbePort;
import ar.com.paas.cluster.status.monitor.domain.exception.ProbeException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;
import ar.com.paas.cluster.status.monitor.domain.model.HealthResult;
import ar.com.paas.cluster.status.monitor.infrastructure.config.CsmProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Adaptador de sondeo de salud contra clústeres OpenShift usando {@link RestClient}
 * (sobre Apache httpclient5). Aplica reintentos con backoff.
 */
@Slf4j
@Component
public class OpenShiftHealthProbeAdapter implements ClusterHealthProbePort {

    private final RestClient restClient;
    private final CsmProperties.Probe probeProps;

    public OpenShiftHealthProbeAdapter(RestClient csmRestClient, CsmProperties properties) {
        this.restClient = csmRestClient;
        this.probeProps = properties.getProbe();
    }

    @Override
    public HealthResult check(ClusterTarget target) {
        int maxRetries = probeProps.getMaxRetries();
        long backoff = probeProps.getRetryBackoffMs();
        RuntimeException last = null;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            long start = System.nanoTime();
            try {
                restClient.get()
                        .uri(target.getHealthUrl())
                        .retrieve()
                        .toBodilessEntity();
                Duration latency = Duration.ofNanos(System.nanoTime() - start);
                return HealthResult.up(latency);
            } catch (RuntimeException ex) {
                last = ex;
                log.debug("Fallo de sondeo cluster '{}' intento {}/{}: {}",
                        target.name(), attempt, maxRetries, ex.getMessage());
                sleep(backoff * (attempt + 1L));
            }
        }

        throw new ProbeException("Sondeo agotado para cluster '" + target.name() + "'", last);
    }

    private void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
