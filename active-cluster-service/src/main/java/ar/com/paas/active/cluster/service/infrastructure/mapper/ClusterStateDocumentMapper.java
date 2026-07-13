package ar.com.paas.active.cluster.service.infrastructure.mapper;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.ClusterStateDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct entre documentos Mongo y el modelo de dominio.
 */
@Mapper(componentModel = "spring")
public interface ClusterStateDocumentMapper {

    ClusterStateDocument toDocument(ClusterState state);

    ClusterState toDomain(ClusterStateDocument document);

    /**
     * Mapea la proyeccion de cache a dominio.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    ClusterState fromCacheData(AppStatusCacheDocument.CacheData data);
}
