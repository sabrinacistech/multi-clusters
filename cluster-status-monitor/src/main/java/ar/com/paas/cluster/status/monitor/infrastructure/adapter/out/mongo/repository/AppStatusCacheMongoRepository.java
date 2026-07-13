package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.repository;

import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppStatusCacheMongoRepository extends MongoRepository<AppStatusCacheDocument, String> {
}
