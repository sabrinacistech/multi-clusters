package com.sabrinacistech.multiclusters.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sabrinacistech.multiclusters.ClusterStatusServiceApplication;
import com.sabrinacistech.multiclusters.model.ClusterStatusCacheDocument;
import com.sabrinacistech.multiclusters.repository.ClusterStatusCacheRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    classes = ClusterStatusServiceApplication.class,
    properties = "spring.autoconfigure.exclude="
        + "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,"
        + "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,"
        + "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration"
)
@AutoConfigureMockMvc
class ClusterStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClusterStatusCacheRepository repository;

    @BeforeEach
    void setUp() {
        ClusterStatusCacheDocument cache = new ClusterStatusCacheDocument();
        cache.setId("default-cache");
        cache.setDataCenter("default");
        cache.setActive(false);
        cache.setPollingIntervalSeconds(45);
        cache.setUpdatedAt(Instant.parse("2026-05-26T00:00:00Z"));

        when(repository.findFirstByDataCenterOrderByUpdatedAtDesc("default"))
            .thenReturn(Optional.of(cache));
    }

    @Test
    void getClusterStatusReturnsCurrentMongoCacheStatus() throws Exception {
        mockMvc.perform(get("/get-cluster-status")
                .header("APP-NAME", "consumer-api")
                .header("PROJECT-NAME", "multi-clusters")
                .header("CLIENT-CLUSTER-ACTIVE", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.metadata.appName", is("consumer-api")))
            .andExpect(jsonPath("$.metadata.projectName", is("multi-clusters")))
            .andExpect(jsonPath("$.metadata.clientClusterActive", is("true")))
            .andExpect(jsonPath("$.data.active", is(false)))
            .andExpect(jsonPath("$.data.pollingIntervalSeconds", is(45)));
    }
}
