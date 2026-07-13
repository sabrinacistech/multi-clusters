package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo;

import ar.com.paas.active.cluster.service.application.port.out.ClusterStateRepositoryPort;
import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.ClusterStateDocument;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.repository.ClusterStateMongoRepository;
import ar.com.paas.active.cluster.service.infrastructure.mapper.ClusterStateDocumentMapper;
import com.mongodb.ReadPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto de repositorio.
 * Usa MongoTemplate con proyeccion y read preference secondaryPreferred para la
 * consulta de estado activo (mayormente lecturas).
 */
@Slf4j
@Component
public class ClusterStatePersistenceAdapter implements ClusterStateRepositoryPort {

    private final ClusterStateMongoRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ClusterStateDocumentMapper mapper;

    public ClusterStatePersistenceAdapter(ClusterStateMongoRepository repository,
                                          MongoTemplate mongoTemplate,
                                          ClusterStateDocumentMapper mapper) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<ClusterState> findActiveByLogicalKey(String application, String project, String environment) {
        Query query = new Query(Criteria.where("application").is(application)
                .and("project").is(project)
                .and("environment").is(environment)
                .and("active").is(true))
                .withReadPreference(ReadPreference.secondaryPreferred());
        query.fields()
                .include("active")
                .include("pollingIntervalSeconds")
                .include("activeCluster")
                .include("application")
                .include("project")
                .include("environment");

        ClusterStateDocument document = mongoTemplate.findOne(query, ClusterStateDocument.class);
        return Optional.ofNullable(document).map(mapper::toDomain);
    }

    @Override
    public ClusterState save(ClusterState state) {
        ClusterStateDocument saved = repository.save(mapper.toDocument(state));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ClusterState> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
