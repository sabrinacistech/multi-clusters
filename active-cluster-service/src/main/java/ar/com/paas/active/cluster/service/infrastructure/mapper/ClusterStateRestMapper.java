package ar.com.paas.active.cluster.service.infrastructure.mapper;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ClusterStateDTO;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct entre el DTO REST y el modelo de dominio.
 */
@Mapper(componentModel = "spring")
public interface ClusterStateRestMapper {

    ClusterStateDTO toDto(ClusterState state);

    ClusterState toDomain(ClusterStateDTO dto);
}
