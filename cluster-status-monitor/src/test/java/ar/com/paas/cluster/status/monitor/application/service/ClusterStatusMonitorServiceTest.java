package ar.com.paas.cluster.status.monitor.application.service;

import ar.com.paas.cluster.status.monitor.application.port.out.ClusterCatalog;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterHealthProbePort;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.application.port.out.StatusCachePort;
import ar.com.paas.cluster.status.monitor.domain.exception.ProbeException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterTarget;
import ar.com.paas.cluster.status.monitor.domain.model.HealthResult;
import ar.com.paas.cluster.status.monitor.domain.model.PollSummary;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClusterStatusMonitorServiceTest {

    @Mock
    private ClusterHealthProbePort healthProbePort;
    @Mock
    private ClusterStatusRepositoryPort repositoryPort;
    @Mock
    private StatusCachePort cachePort;
    @Mock
    private ClusterCatalog clusterCatalog;

    private ClusterStatusMonitorService service;

    @BeforeEach
    void setUp() {
        service = new ClusterStatusMonitorService(
                clusterCatalog, healthProbePort, repositoryPort, cachePort, new SimpleMeterRegistry());
    }

    @Test
    void pollAll_persisteYRefrescaCache_paraCadaClusterSano() {
        ClusterTarget t1 = new ClusterTarget("app-a", "ocp-01", "https://ocp-01/healthz");
        ClusterTarget t2 = new ClusterTarget("app-b", "ocp-02", "https://ocp-02/healthz");
        when(clusterCatalog.registered()).thenReturn(List.of(t1, t2));
        when(healthProbePort.check(any())).thenReturn(HealthResult.up());

        PollSummary summary = service.pollAll();

        assertThat(summary.getSucceeded()).isEqualTo(2);
        assertThat(summary.getFailed()).isEqualTo(0);
        verify(repositoryPort, times(2)).upsert(any(ClusterStatus.class));
        verify(cachePort, times(2)).refresh(anyString(), any(ClusterStatus.class));
    }

    @Test
    void pollAll_aislaFallosPorCluster_sinAbortarElCiclo() {
        ClusterTarget ok = new ClusterTarget("app-a", "ocp-01", "https://ocp-01/healthz");
        ClusterTarget bad = new ClusterTarget("app-b", "ocp-02", "https://ocp-02/healthz");
        when(clusterCatalog.registered()).thenReturn(List.of(ok, bad));
        when(healthProbePort.check(ok)).thenReturn(HealthResult.up());
        when(healthProbePort.check(bad)).thenThrow(new ProbeException("timeout"));

        PollSummary summary = service.pollAll();

        assertThat(summary.getSucceeded()).isEqualTo(1);
        assertThat(summary.getFailed()).isEqualTo(1);
        verify(repositoryPort, times(1)).upsert(any(ClusterStatus.class));
        verify(cachePort, times(1)).refresh(anyString(), any(ClusterStatus.class));
    }
}
