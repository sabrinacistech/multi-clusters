package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Documento Mongo del estado de cluster (coleccion app_cluster_status).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_cluster_status")
public class ClusterStateDocument {

    @Id
    private String id;

    @Indexed
    private String application;

    @Indexed
    private String project;

    @Indexed
    private String environment;

    private String activeCluster;

    @Indexed
    private Boolean active;

    private Integer pollingIntervalSeconds;

    private String updatedBy;

    private Instant updatedAt;

    private String reason;

    private Map<String, Object> metadata;
}
