package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo;

import ar.com.paas.cluster.status.monitor.application.port.out.ClusterStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.domain.exception.PersistenceException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.ClusterStatusDocument;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.repository.ClusterStatusMongoRepository;
import ar.com.paas.cluster.status.monitor.infrastructure.config.CsmProperties;
import ar.com.paas.cluster.status.monitor.infrastructure.mapper.ClusterStatusDocumentMapper;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para estados de clúster.
 * <p>
 * El upsert respeta una ventana de gracia manual: si el registro fue modificado
 * manualmente ({@code updatedBy=MANUAL}) dentro de {@code csm.manual-grace-seconds},
 * el ciclo programado no lo sobreescribe.
 */
@Slf4j
@Component
public class ClusterStatusPersistenceAdapter implements ClusterStatusRepositoryPort {

    private static final int MAX_WRITE_RETRIES = 3;

    private final MongoTemplate mongoTemplate;
    private final ClusterStatusMongoRepository repository;
    private final ClusterStatusDocumentMapper mapper;
    private final CsmProperties properties;
    private final MeterRegistry meterRegistry;

    public ClusterStatusPersistenceAdapter(MongoTemplate mongoTemplate,
                                           ClusterStatusMongoRepository repository,
                                           ClusterStatusDocumentMapper mapper,
                                           CsmProperties properties,
                                           MeterRegistry meterRegistry) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
        this.mapper = mapper;
        this.properties = properties;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public ClusterStatus upsert(ClusterStatus status) {
        int attempt = 0;
        while (true) {
            try {
                Query query = new Query(Criteria.where("codigo").is(status.getCodigo())
                        .and("app").is(status.getApp())
                        .and("cluster").is(status.getCluster()));

                // Respeta la ventana de gracia manual: no sobreescribir cambios manuales recientes.
                ClusterStatusDocument existing = mongoTemplate.findOne(query, ClusterStatusDocument.class);
                if (existing != null && isWithinManualGrace(existing)) {
                    log.debug("Se omite upsert por ventana de gracia manual para {}", status.cacheKey());
                    return mapper.toDomain(existing);
                }

                Update update = new Update()
                        .set("codigo", status.getCodigo())
                        .set("app", status.getApp())
                        .set("cluster", status.getCluster())
                        .set("isActive", status.getIsActive())
                        .set("updatedBy", status.getUpdatedBy())
                        .set("updatedAt", status.getUpdatedAt() != null ? status.getUpdatedAt() : Instant.now());

                mongoTemplate.upsert(query, update, ClusterStatusDocument.class);

                ClusterStatusDocument saved = mongoTemplate.findOne(query, ClusterStatusDocument.class);
                return saved != null ? mapper.toDomain(saved) : status;
            } catch (OptimisticLockingFailureException ex) {
                attempt++;
                meterRegistry.counter("csm_write_conflict_total", "source", "schedule").increment();
                if (attempt >= MAX_WRITE_RETRIES) {
                    throw new PersistenceException("Conflicto de escritura tras reintentos: "
                            + status.cacheKey(), ex);
                }
                log.warn("Conflicto optimista en upsert (intento {}), reintentando: {}", attempt, status.cacheKey());
            } catch (DataAccessException ex) {
                throw new PersistenceException("Error de persistencia en upsert: " + status.cacheKey(), ex);
            }
        }
    }

    @Override
    public List<ClusterStatus> findAll() {
        try {
            return repository.findAll().stream().map(mapper::toDomain).toList();
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error listando estados de cluster", ex);
        }
    }

    @Override
    public Optional<ClusterStatus> findById(String id) {
        try {
            return repository.findById(id).map(mapper::toDomain);
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error buscando estado de cluster: " + id, ex);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            repository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error eliminando estado de cluster: " + id, ex);
        }
    }

    @Override
    public ClusterStatus save(ClusterStatus status) {
        try {
            ClusterStatusDocument doc = mapper.toDocument(status);
            return mapper.toDomain(repository.save(doc));
        } catch (OptimisticLockingFailureException ex) {
            meterRegistry.counter("csm_write_conflict_total", "source", "abm").increment();
            throw new PersistenceException("Conflicto de escritura: " + status.cacheKey(), ex);
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error guardando estado de cluster", ex);
        }
    }

    private boolean isWithinManualGrace(ClusterStatusDocument doc) {
        if (!ClusterStatus.UPDATED_BY_MANUAL.equals(doc.getUpdatedBy())) {
            return false;
        }
        Instant ref = doc.getLastManualAt() != null ? doc.getLastManualAt() : doc.getUpdatedAt();
        if (ref == null) {
            return false;
        }
        Instant limit = ref.plusSeconds(properties.getManualGraceSeconds());
        return Instant.now().isBefore(limit);
    }
}
