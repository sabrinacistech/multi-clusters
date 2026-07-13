package ar.com.paas.cluster.status.monitor.application.port.out;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;

/**
 * Puerto de salida para el cache de estados (colección con TTL).
 */
public interface StatusCachePort {

    /**
     * Refresca (upsert) la entrada de cache para la clave dada.
     */
    void refresh(String cacheKey, ClusterStatus status);

    /**
     * Invalida (elimina) la entrada de cache para la clave dada.
     */
    void evict(String cacheKey);
}
