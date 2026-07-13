package ar.com.paas.cluster.status.monitor.application.service;

import ar.com.paas.cluster.status.monitor.application.port.in.ManageClusterStatusUseCase;
import ar.com.paas.cluster.status.monitor.application.port.out.ClusterStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.application.port.out.StatusCachePort;
import ar.com.paas.cluster.status.monitor.domain.exception.BusinessRuleException;
import ar.com.paas.cluster.status.monitor.domain.exception.ConflictException;
import ar.com.paas.cluster.status.monitor.domain.exception.NotFoundException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Servicio ABM de estados de clúster. Aplica reglas de negocio y persiste vía puerto.
 */
@Slf4j
@Service
public class AbmClusterStatusService implements ManageClusterStatusUseCase {

    private final ClusterStatusRepositoryPort repositoryPort;
    private final StatusCachePort cachePort;
    private final MeterRegistry meterRegistry;

    public AbmClusterStatusService(ClusterStatusRepositoryPort repositoryPort,
                                   StatusCachePort cachePort,
                                   MeterRegistry meterRegistry) {
        this.repositoryPort = repositoryPort;
        this.cachePort = cachePort;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public List<ClusterStatus> list() {
        count("list", "success");
        return repositoryPort.findAll();
    }

    @Override
    public ClusterStatus get(String id) {
        ClusterStatus found = repositoryPort.findById(id)
                .orElseThrow(() -> {
                    count("get", "not_found");
                    return new NotFoundException("Estado de cluster no encontrado: " + id);
                });
        count("get", "success");
        return found;
    }

    @Override
    public ClusterStatus create(ClusterStatus s) {
        try {
            List<ClusterStatus> all = repositoryPort.findAll();

            // Clave lógica duplicada -> CSM-009.
            boolean duplicate = all.stream()
                    .anyMatch(existing -> existing.cacheKey().equals(s.cacheKey()));
            if (duplicate) {
                count("create", "conflict");
                throw new ConflictException("Ya existe un estado con la clave logica: " + s.cacheKey());
            }

            enforceSingleActivePerApp(all, s, null);

            ClusterStatus toPersist = s.withUpdatedBy(resolveUpdatedBy(s))
                    .withUpdatedAt(Instant.now());
            ClusterStatus saved = repositoryPort.save(toPersist);
            cachePort.refresh(saved.cacheKey(), saved);
            count("create", "success");
            return saved;
        } catch (BusinessRuleException | ConflictException ex) {
            throw ex;
        }
    }

    @Override
    public ClusterStatus update(String id, ClusterStatus s) {
        ClusterStatus existing = repositoryPort.findById(id)
                .orElseThrow(() -> {
                    count("update", "not_found");
                    return new NotFoundException("Estado de cluster no encontrado: " + id);
                });

        List<ClusterStatus> all = repositoryPort.findAll();
        enforceSingleActivePerApp(all, s, id);

        ClusterStatus merged = existing
                .withCodigo(s.getCodigo())
                .withApp(s.getApp())
                .withCluster(s.getCluster())
                .withIsActive(s.getIsActive())
                .withUpdatedBy(resolveUpdatedBy(s))
                .withUpdatedAt(Instant.now());

        ClusterStatus saved = repositoryPort.save(merged);
        cachePort.refresh(saved.cacheKey(), saved);
        count("update", "success");
        return saved;
    }

    @Override
    public void delete(String id) {
        ClusterStatus existing = repositoryPort.findById(id)
                .orElseThrow(() -> {
                    count("delete", "not_found");
                    return new NotFoundException("Estado de cluster no encontrado: " + id);
                });
        repositoryPort.deleteById(id);
        cachePort.evict(existing.cacheKey());
        count("delete", "success");
    }

    /**
     * Regla de negocio: no puede haber dos clústeres activos para la misma app.
     */
    private void enforceSingleActivePerApp(List<ClusterStatus> all, ClusterStatus candidate, String excludeId) {
        if (candidate.getIsActive() == null || !candidate.getIsActive()) {
            return;
        }
        boolean otherActive = all.stream()
                .filter(e -> excludeId == null || !excludeId.equals(e.getId()))
                .filter(e -> candidate.getApp() != null && candidate.getApp().equals(e.getApp()))
                .filter(e -> !e.cacheKey().equals(candidate.cacheKey()))
                .anyMatch(e -> Boolean.TRUE.equals(e.getIsActive()));
        if (otherActive) {
            count("create", "business_rule");
            throw new BusinessRuleException(
                    "No pueden existir dos clusteres activos para la misma app: " + candidate.getApp());
        }
    }

    private String resolveUpdatedBy(ClusterStatus s) {
        return s.getUpdatedBy() != null ? s.getUpdatedBy() : ClusterStatus.UPDATED_BY_MANUAL;
    }

    private void count(String op, String result) {
        meterRegistry.counter("csm_abm_requests_total", "op", op, "result", result).increment();
    }
}
