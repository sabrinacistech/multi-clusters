package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo;

import ar.com.paas.active.cluster.service.application.port.out.StatusCacheReaderPort;
import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.repository.AppStatusCacheMongoRepository;
import ar.com.paas.active.cluster.service.infrastructure.mapper.ClusterStateDocumentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

/**
 * Adaptador de solo lectura de la cache de estados (app_status_cache).
 * Respeta el TTL comprobando expiresAt; nunca escribe.
 */
@Slf4j
@Component
public class StatusCacheReaderAdapter implements StatusCacheReaderPort {

    private final AppStatusCacheMongoRepository repository;
    private final ClusterStateDocumentMapper mapper;

    public StatusCacheReaderAdapter(AppStatusCacheMongoRepository repository,
                                    ClusterStateDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ClusterState> readByCacheKey(String cacheKey) {
        return repository.findByCacheKey(cacheKey)
                .filter(this::isNotExpired)
                .map(AppStatusCacheDocument::getData)
                .map(mapper::fromCacheData);
    }

    private boolean isNotExpired(AppStatusCacheDocument document) {
        Instant expiresAt = document.getExpiresAt();
        return expiresAt == null || expiresAt.isAfter(Instant.now());
    }
}
