package ar.com.paas.cluster.status.monitor.infrastructure.mapper;

import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.AliasStatusDocument;
import org.mapstruct.Mapper;

/**
 * Mapper Documento &lt;-&gt; dominio para estado de alias.
 */
@Mapper(componentModel = "spring")
public interface AliasStatusDocumentMapper {

    AliasStatusDocument toDocument(AliasStatus domain);

    AliasStatus toDomain(AliasStatusDocument document);
}
