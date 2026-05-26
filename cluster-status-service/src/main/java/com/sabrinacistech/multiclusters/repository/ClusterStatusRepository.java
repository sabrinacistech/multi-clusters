package com.sabrinacistech.multiclusters.repository;

import com.sabrinacistech.multiclusters.model.ClusterStatusDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClusterStatusRepository extends MongoRepository<ClusterStatusDocument, String> {

    Optional<ClusterStatusDocument> findFirstByDataCenterOrderByUpdatedAtDesc(String dataCenter);
}
