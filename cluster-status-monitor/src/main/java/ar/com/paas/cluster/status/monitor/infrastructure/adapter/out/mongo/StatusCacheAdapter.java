package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo;

import ar.com.paas.cluster.status.monitor.application.port.out.StatusCachePort;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import ar.com.paas.cluster.status.monitor.infrastructure.mapper.CacheProjectionMapper;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Adaptador de cache de estados sobre la colección {@code app_status_cache} con TTL.
 * Implementa refresh/evict según §7.8 del SDD.
 */
@Slf4j
@Component
public class StatusCacheAdapter implements StatusCachePort {

    private final MongoTemplate mongoTemplate;
    private final CacheProjectionMapper cacheMapper;
    private final Duration ttl;
    private final MeterRegistry meterRegistry;

    public StatusCacheAdapter(MongoTemplate mongoTemplate,
                              CacheProjectionMapper cacheMapper,
                              @Qualifier("cacheTtl") Duration ttl,
                              MeterRegistry meterRegistry) {
        this.mongoTemplate = mongoTemplate;
        this.cacheMapper = cacheMapper;
        this.ttl = ttl;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void refresh(String cacheKey, ClusterStatus status) {
        Instant now = Instant.now();
        AppStatusCacheDocument.CacheData data = cacheMapper.toCacheData(status);

        Query query = new Query(Criteria.where("cacheKey").is(cacheKey));
        Update update = new Update()
                .set("cacheKey", cacheKey)
                .set("data", data)
                .set("cachedAt", now)
                .set("expiresAt", now.plus(ttl));

        mongoTemplate.upsert(query, update, AppStatusCacheDocument.class);
        meterRegistry.counter("csm_cache_refresh_total", "result", "updated").increment();
        log.debug("Cache refrescado para {}", cacheKey);
    }

    @Override
    public void evict(String cacheKey) {
        Query query = new Query(Criteria.where("cacheKey").is(cacheKey));
        mongoTemplate.remove(query, AppStatusCacheDocument.class);
        meterRegistry.counter("csm_cache_refresh_total", "result", "invalidated").increment();
        log.debug("Cache invalidado para {}", cacheKey);
    }
}
