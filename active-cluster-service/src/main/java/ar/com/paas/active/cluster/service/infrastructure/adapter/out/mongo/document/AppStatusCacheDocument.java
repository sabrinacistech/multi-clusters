package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Documento Mongo de la cache de estados (coleccion app_status_cache).
 * El documento expira automaticamente segun expiresAt (TTL).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_status_cache")
public class AppStatusCacheDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String cacheKey;

    private CacheData data;

    private Instant cachedAt;

    @Indexed(expireAfter = "0s")
    private Instant expiresAt;

    /**
     * Proyeccion embebida con los campos relevantes del estado.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheData {
        private Boolean active;
        private Integer pollingIntervalSeconds;
        private String activeCluster;
        private String application;
        private String project;
        private String environment;
    }
}
