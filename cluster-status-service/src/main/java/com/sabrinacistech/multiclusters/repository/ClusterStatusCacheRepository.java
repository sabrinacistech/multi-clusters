package com.sabrinacistech.multiclusters.repository;

import com.sabrinacistech.multiclusters.model.ClusterStatusCacheDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClusterStatusCacheRepository extends MongoRepository<ClusterStatusCacheDocument, String> {

    Optional<ClusterStatusCacheDocument> findFirstByDataCenterOrderByUpdatedAtDesc(String dataCenter);
}
