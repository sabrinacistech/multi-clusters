package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.repository;

import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.AppStatusCacheDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio Mongo de la cache de estados (solo lectura desde el READER).
 */
public interface AppStatusCacheMongoRepository extends MongoRepository<AppStatusCacheDocument, String> {

    Optional<AppStatusCacheDocument> findByCacheKey(String cacheKey);
}
