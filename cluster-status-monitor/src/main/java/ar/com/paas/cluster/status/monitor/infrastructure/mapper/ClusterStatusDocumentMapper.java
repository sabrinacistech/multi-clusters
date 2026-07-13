package ar.com.paas.cluster.status.monitor.infrastructure.mapper;

import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.ClusterStatusDocument;
import org.mapstruct.Mapper;

/**
 * Mapper Documento &lt;-&gt; dominio para estado de clúster.
 */
@Mapper(componentModel = "spring")
public interface ClusterStatusDocumentMapper {

    ClusterStatusDocument toDocument(ClusterStatus domain);

    ClusterStatus toDomain(ClusterStatusDocument document);
}
