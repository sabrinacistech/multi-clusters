package ar.com.paas.cluster.status.monitor.infrastructure.mapper;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import org.mapstruct.Mapper;

/**
 * Mapper de proyección de estado -&gt; datos embebidos de cache.
 */
@Mapper(componentModel = "spring")
public interface CacheProjectionMapper {

    AppStatusCacheDocument.CacheData toCacheData(ClusterStatus status);
}
