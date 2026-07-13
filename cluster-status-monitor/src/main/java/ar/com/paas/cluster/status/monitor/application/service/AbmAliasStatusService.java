package ar.com.paas.cluster.status.monitor.application.service;

import ar.com.paas.cluster.status.monitor.application.port.in.ManageAliasStatusUseCase;
import ar.com.paas.cluster.status.monitor.application.port.out.AliasStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.domain.exception.ConflictException;
import ar.com.paas.cluster.status.monitor.domain.exception.NotFoundException;
import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Servicio ABM de estados de alias.
 */
@Slf4j
@Service
public class AbmAliasStatusService implements ManageAliasStatusUseCase {

    private final AliasStatusRepositoryPort repositoryPort;
    private final MeterRegistry meterRegistry;

    public AbmAliasStatusService(AliasStatusRepositoryPort repositoryPort, MeterRegistry meterRegistry) {
        this.repositoryPort = repositoryPort;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public List<AliasStatus> list() {
        count("list", "success");
        return repositoryPort.findAll();
    }

    @Override
    public AliasStatus create(AliasStatus s) {
        if (repositoryPort.findByAlias(s.getAlias()).isPresent()) {
            count("create", "conflict");
            throw new ConflictException("Ya existe el alias: " + s.getAlias());
        }
        AliasStatus saved = repositoryPort.save(s.withUpdatedAt(Instant.now()));
        count("create", "success");
        return saved;
    }

    @Override
    public AliasStatus update(String alias, AliasStatus s) {
        AliasStatus existing = repositoryPort.findByAlias(alias)
                .orElseThrow(() -> {
                    count("update", "not_found");
                    return new NotFoundException("Alias no encontrado: " + alias);
                });
        AliasStatus merged = existing
                .withApp(s.getApp())
                .withCluster(s.getCluster())
                .withTarget(s.getTarget())
                .withIsActive(s.getIsActive())
                .withUpdatedAt(Instant.now());
        AliasStatus saved = repositoryPort.save(merged);
        count("update", "success");
        return saved;
    }

    @Override
    public void delete(String alias) {
        repositoryPort.findByAlias(alias)
                .orElseThrow(() -> {
                    count("delete", "not_found");
                    return new NotFoundException("Alias no encontrado: " + alias);
                });
        repositoryPort.deleteByAlias(alias);
        count("delete", "success");
    }

    private void count(String op, String result) {
        meterRegistry.counter("csm_abm_alias_requests_total", "op", op, "result", result).increment();
    }
}
