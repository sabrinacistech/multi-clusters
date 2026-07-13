package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Documento Mongo para el estado de clúster. Colección {@code app_cluster_status}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_cluster_status")
public class ClusterStatusDocument {

    @Id
    private String id;

    @Indexed
    private String codigo;

    private String app;
    private String cluster;
    private Boolean isActive;
    private String updatedBy;
    private Instant updatedAt;
    private Instant lastManualAt;

    @Version
    private Long version;
}
