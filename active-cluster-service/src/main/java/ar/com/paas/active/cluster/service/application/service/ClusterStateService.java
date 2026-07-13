package ar.com.paas.active.cluster.service.application.service;

import ar.com.paas.active.cluster.service.application.port.in.ClusterStateCommand;
import ar.com.paas.active.cluster.service.application.port.in.CreateClusterStateUseCase;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase;
import ar.com.paas.active.cluster.service.application.port.in.RegisterInitialUseCase;
import ar.com.paas.active.cluster.service.application.port.in.UpdateClusterStateUseCase;
import ar.com.paas.active.cluster.service.application.port.out.ClusterStateRepositoryPort;
import ar.com.paas.active.cluster.service.application.port.out.StatusCacheReaderPort;
import ar.com.paas.active.cluster.service.domain.exception.ClusterStateNotFoundException;
import ar.com.paas.active.cluster.service.domain.exception.InvalidParametersException;
import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Servicio de aplicacion que implementa todos los casos de uso del READER.
 */
@Slf4j
@Service
public class ClusterStateService implements
        GetClusterStatusUseCase,
        RegisterInitialUseCase,
        CreateClusterStateUseCase,
        UpdateClusterStateUseCase {

    private static final int DEFAULT_POLLING_INTERVAL_SECONDS = 30;

    private final ClusterStateRepositoryPort repositoryPort;
    private final StatusCacheReaderPort cacheReaderPort;
    private final MeterRegistry meterRegistry;
    private final Timer queryLatencyTimer;

    public ClusterStateService(ClusterStateRepositoryPort repositoryPort,
                               StatusCacheReaderPort cacheReaderPort,
                               MeterRegistry meterRegistry) {
        this.repositoryPort = repositoryPort;
        this.cacheReaderPort = cacheReaderPort;
        this.meterRegistry = meterRegistry;
        this.queryLatencyTimer = Timer.builder("acs_query_latency_seconds")
                .description("Latencia de las consultas de estado de cluster")
                .register(meterRegistry);
    }

    @Override
    public ClusterStatusResult getStatus(GetClusterStatusQuery query) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            validateQuery(query);

            String cacheKey = buildCacheKey(query.application(), query.project(), query.environment());

            Optional<ClusterState> cached = cacheReaderPort.readByCacheKey(cacheKey);
            ClusterState state = cached.orElseGet(() ->
                    repositoryPort.findActiveByLogicalKey(
                                    query.application(), query.project(), query.environment())
                            .orElseThrow(() -> new ClusterStateNotFoundException(
                                    "No se encontro estado de cluster para la clave logica: " + cacheKey)));

            incrementResultCounter("found");
            int polling = state.getPollingIntervalSeconds() != null
                    ? state.getPollingIntervalSeconds()
                    : DEFAULT_POLLING_INTERVAL_SECONDS;
            return new ClusterStatusResult(Boolean.TRUE.equals(state.getActive()), polling);
        } catch (ClusterStateNotFoundException e) {
            incrementResultCounter("not_found");
            throw e;
        } catch (InvalidParametersException e) {
            incrementResultCounter("error");
            throw e;
        } catch (RuntimeException e) {
            incrementResultCounter("error");
            throw e;
        } finally {
            sample.stop(queryLatencyTimer);
        }
    }

    @Override
    public ClusterState registerInitial(ClusterStateCommand command) {
        return persist(command);
    }

    @Override
    public ClusterState create(ClusterStateCommand command) {
        return persist(command);
    }

    @Override
    public ClusterState update(ClusterStateCommand command) {
        return persist(command);
    }

    private ClusterState persist(ClusterStateCommand command) {
        ClusterState state = ClusterState.of(ClusterState.builder()
                .id(command.id())
                .application(command.application())
                .project(command.project())
                .environment(command.environment())
                .activeCluster(command.activeCluster())
                .active(command.active())
                .pollingIntervalSeconds(command.pollingIntervalSeconds() != null
                        ? command.pollingIntervalSeconds()
                        : DEFAULT_POLLING_INTERVAL_SECONDS)
                .updatedBy(command.updatedBy())
                .updatedAt(Instant.now())
                .reason(command.reason())
                .metadata(command.metadata()));
        return repositoryPort.save(state);
    }

    private void validateQuery(GetClusterStatusQuery query) {
        if (query == null
                || isBlank(query.application())
                || isBlank(query.project())
                || isBlank(query.environment())) {
            throw new InvalidParametersException(
                    "Los parametros application, project y environment son obligatorios");
        }
    }

    private void incrementResultCounter(String result) {
        Counter.builder("acs_query_requests_total")
                .tag("result", result)
                .register(meterRegistry)
                .increment();
    }

    private String buildCacheKey(String application, String project, String environment) {
        return application + ":" + project + ":" + environment;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
