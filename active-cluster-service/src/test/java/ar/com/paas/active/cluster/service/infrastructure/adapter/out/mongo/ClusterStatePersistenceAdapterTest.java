package ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo;

import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.document.ClusterStateDocument;
import ar.com.paas.active.cluster.service.infrastructure.adapter.out.mongo.repository.ClusterStateMongoRepository;
import ar.com.paas.active.cluster.service.infrastructure.mapper.ClusterStateDocumentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClusterStatePersistenceAdapterTest {

    @Mock
    private ClusterStateMongoRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private ClusterStatePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ClusterStatePersistenceAdapter(
                repository, mongoTemplate, org.mapstruct.factory.Mappers.getMapper(ClusterStateDocumentMapper.class));
    }

    private ClusterStateDocument sampleDocument() {
        return ClusterStateDocument.builder()
                .id("1")
                .application("app")
                .project("proj")
                .environment("dev")
                .activeCluster("cluster-a")
                .active(true)
                .pollingIntervalSeconds(30)
                .updatedBy("tester")
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void findActiveByLogicalKeyReturnsMappedDomain() {
        when(mongoTemplate.findOne(any(Query.class), eq(ClusterStateDocument.class)))
                .thenReturn(sampleDocument());

        Optional<ClusterState> result = adapter.findActiveByLogicalKey("app", "proj", "dev");

        assertThat(result).isPresent();
        assertThat(result.get().getActive()).isTrue();
        assertThat(result.get().getPollingIntervalSeconds()).isEqualTo(30);
    }

    @Test
    void findActiveByLogicalKeyReturnsEmptyWhenNoDocument() {
        when(mongoTemplate.findOne(any(Query.class), eq(ClusterStateDocument.class)))
                .thenReturn(null);

        Optional<ClusterState> result = adapter.findActiveByLogicalKey("app", "proj", "dev");

        assertThat(result).isEmpty();
    }

    @Test
    void saveReturnsMappedDomain() {
        when(repository.save(any(ClusterStateDocument.class))).thenReturn(sampleDocument());

        ClusterState state = ClusterState.builder()
                .application("app").project("proj").environment("dev")
                .activeCluster("cluster-a").active(true).pollingIntervalSeconds(30)
                .updatedBy("tester").updatedAt(Instant.now()).build();

        ClusterState saved = adapter.save(state);

        assertThat(saved.getApplication()).isEqualTo("app");
    }
}
