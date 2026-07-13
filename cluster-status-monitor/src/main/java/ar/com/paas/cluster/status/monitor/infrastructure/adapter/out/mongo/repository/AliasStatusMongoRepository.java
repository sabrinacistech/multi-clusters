package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.repository;

import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.AliasStatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AliasStatusMongoRepository extends MongoRepository<AliasStatusDocument, String> {

    Optional<AliasStatusDocument> findByAlias(String alias);

    void deleteByAlias(String alias);
}
