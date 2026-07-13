package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest;

import ar.com.paas.cluster.status.monitor.application.port.in.ManageClusterStatusUseCase;
import ar.com.paas.cluster.status.monitor.domain.exception.NotFoundException;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AbmClusterStatusControllerTest {

    @Mock
    private ManageClusterStatusUseCase useCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AbmClusterStatusController controller = new AbmClusterStatusController(useCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CsmExceptionHandler())
                .build();
    }

    @Test
    void createClusterStatus_devuelve201YSobre() throws Exception {
        ClusterStatus created = new ClusterStatus("id-1", "cod-1", "app-a", "ocp-01", true);
        when(useCase.create(any(ClusterStatus.class))).thenReturn(created);

        String body = "{\"codigo\":\"cod-1\",\"app\":\"app-a\",\"cluster\":\"ocp-01\",\"isActive\":true}";

        mockMvc.perform(post("/api/v1/cluster-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meta.method").value("POST"))
                .andExpect(jsonPath("$.data.app").value("app-a"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    void createClusterStatus_payloadInvalido_devuelve400() throws Exception {
        mockMvc.perform(post("/api/v1/cluster-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].code").value("CSM-001"));
    }

    @Test
    void deleteClusterStatus_inexistente_devuelve404() throws Exception {
        doThrow(new NotFoundException("no existe")).when(useCase).delete("missing");

        mockMvc.perform(delete("/api/v1/cluster-status/{id}", "missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].code").value("CSM-004"));
    }
}
