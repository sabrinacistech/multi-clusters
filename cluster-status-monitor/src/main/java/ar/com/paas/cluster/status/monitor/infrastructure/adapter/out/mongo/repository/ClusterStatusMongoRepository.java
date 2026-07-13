package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.repository;

import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.document.ClusterStatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClusterStatusMongoRepository extends MongoRepository<ClusterStatusDocument, String> {

    List<ClusterStatusDocument> findByCodigoAndAppAndCluster(String codigo, String app, String cluster);
}
