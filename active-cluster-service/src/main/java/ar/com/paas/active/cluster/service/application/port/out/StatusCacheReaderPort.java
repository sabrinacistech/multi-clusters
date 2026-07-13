package ar.com.paas.active.cluster.service.application.port.out;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;

import java.util.Optional;

/**
 * Puerto de salida de solo lectura hacia la cache de estados (app_status_cache).
 * Respeta el TTL configurado y nunca escribe.
 */
public interface StatusCacheReaderPort {

    Optional<ClusterState> readByCacheKey(String cacheKey);
}
