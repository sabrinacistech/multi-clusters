package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Documento de cache con TTL. Colección {@code app_status_cache}.
 * El índice sobre {@code expiresAt} con expireAfter=0s hace que Mongo elimine el
 * documento en cuanto {@code expiresAt} queda en el pasado.
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

    /** Proyección embebida del estado. */
    private CacheData data;

    private Instant cachedAt;

    @Indexed(expireAfter = "0s")
    private Instant expiresAt;

    /**
     * Proyección embebida del estado en el cache.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheData {
        private String codigo;
        private String app;
        private String cluster;
        private Boolean isActive;
        private String updatedBy;
        private Instant updatedAt;
    }
}
