package ar.com.paas.active.cluster.service.application.service;

import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.ClusterStatusResult;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.GetClusterStatusQuery;
import ar.com.paas.active.cluster.service.application.port.out.ClusterStateRepositoryPort;
import ar.com.paas.active.cluster.service.application.port.out.StatusCacheReaderPort;
import ar.com.paas.active.cluster.service.domain.exception.ClusterStateNotFoundException;
import ar.com.paas.active.cluster.service.domain.exception.InvalidParametersException;
import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClusterStateServiceTest {

    @Mock
    private ClusterStateRepositoryPort repositoryPort;
    @Mock
    private StatusCacheReaderPort cacheReaderPort;

    private ClusterStateService service;

    @BeforeEach
    void setUp() {
        service = new ClusterStateService(repositoryPort, cacheReaderPort, new SimpleMeterRegistry());
    }

    private ClusterState sampleState(boolean active, int polling) {
        return ClusterState.builder()
                .id("1")
                .application("app")
                .project("proj")
                .environment("dev")
                .activeCluster("cluster-a")
                .active(active)
                .pollingIntervalSeconds(polling)
                .updatedBy("tester")
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void cacheHitReturnsCachedValue() {
        when(cacheReaderPort.readByCacheKey("app:proj:dev"))
                .thenReturn(Optional.of(sampleState(true, 45)));

        ClusterStatusResult result = service.getStatus(
                new GetClusterStatusQuery("app", "proj", "dev", "cluster-a"));

        assertThat(result.active()).isTrue();
        assertThat(result.pollingIntervalSeconds()).isEqualTo(45);
        verify(repositoryPort, never()).findActiveByLogicalKey("app", "proj", "dev");
    }

    @Test
    void cacheMissFallsBackToRepository() {
        when(cacheReaderPort.readByCacheKey("app:proj:dev")).thenReturn(Optional.empty());
        when(repositoryPort.findActiveByLogicalKey("app", "proj", "dev"))
                .thenReturn(Optional.of(sampleState(true, 30)));

        ClusterStatusResult result = service.getStatus(
                new GetClusterStatusQuery("app", "proj", "dev", "cluster-a"));

        assertThat(result.active()).isTrue();
        assertThat(result.pollingIntervalSeconds()).isEqualTo(30);
    }

    @Test
    void notFoundThrowsClusterStateNotFound() {
        when(cacheReaderPort.readByCacheKey("app:proj:dev")).thenReturn(Optional.empty());
        when(repositoryPort.findActiveByLogicalKey("app", "proj", "dev")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getStatus(
                new GetClusterStatusQuery("app", "proj", "dev", "cluster-a")))
                .isInstanceOf(ClusterStateNotFoundException.class);
    }

    @Test
    void blankParamsThrowInvalidParameters() {
        assertThatThrownBy(() -> service.getStatus(
                new GetClusterStatusQuery("  ", "proj", "dev", "cluster-a")))
                .isInstanceOf(InvalidParametersException.class);
    }
}
