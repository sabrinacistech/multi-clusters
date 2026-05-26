package com.sabrinacistech.multiclusters.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sabrinacistech.multiclusters.ClusterStatusServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ClusterStatusServiceApplication.class)
@AutoConfigureMockMvc
class ClusterStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClusterStatusReturnsCurrentInMemoryStatus() throws Exception {
        mockMvc.perform(get("/get-cluster-status")
                .header("APP-NAME", "consumer-api")
                .header("PROJECT-NAME", "multi-clusters")
                .header("CLIENT-CLUSTER-ACTIVE", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.metadata.appName", is("consumer-api")))
            .andExpect(jsonPath("$.metadata.projectName", is("multi-clusters")))
            .andExpect(jsonPath("$.metadata.clientClusterActive", is("true")))
            .andExpect(jsonPath("$.data.active", is(true)))
            .andExpect(jsonPath("$.data.pollingIntervalSeconds", is(30)));
    }
}
