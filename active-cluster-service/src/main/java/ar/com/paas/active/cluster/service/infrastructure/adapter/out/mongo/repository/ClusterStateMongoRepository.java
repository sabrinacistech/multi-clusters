package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.repository;

import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.ClusterStateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio Mongo del estado de cluster.
 */
public interface ClusterStateMongoRepository extends MongoRepository<ClusterStateDocument, String> {

    Optional<ClusterStateDocument> findFirstByApplicationAndProjectAndEnvironmentAndActiveIsTrue(
            String application, String project, String environment);
}
