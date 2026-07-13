package ar.com.paas.cluster.status.monitor.infrastructure.mapper;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ClusterStatusDTO;
import org.mapstruct.Mapper;

/**
 * Mapper DTO &lt;-&gt; dominio para estado de clúster (uso en documentación / capas auxiliares).
 */
@Mapper(componentModel = "spring")
public interface ClusterStatusDtoMapper {

    ClusterStatusDTO toDto(ClusterStatus domain);
}
