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
 * Documento Mongo para el estado de alias. Colección {@code alias_status}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alias_status")
public class AliasStatusDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String alias;

    private String app;
    private String cluster;
    private String target;
    private Boolean isActive;
    private Instant updatedAt;
}
