package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest;

import ar.com.paas.active.cluster.service.application.port.in.CreateClusterStateUseCase;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.ClusterStatusResult;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.GetClusterStatusQuery;
import ar.com.paas.active.cluster.service.application.port.in.RegisterInitialUseCase;
import ar.com.paas.active.cluster.service.application.port.in.UpdateClusterStateUseCase;
import ar.com.paas.active.cluster.service.domain.exception.ClusterStateNotFoundException;
import ar.com.paas.active.cluster.service.domain.exception.InvalidParametersException;
import ar.com.paas.active.cluster.service.infrastructure.mapper.ClusterStateRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClusterStateControllerTest {

    private GetClusterStatusUseCase getClusterStatusUseCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        getClusterStatusUseCase = Mockito.mock(GetClusterStatusUseCase.class);
        RegisterInitialUseCase registerInitialUseCase = Mockito.mock(RegisterInitialUseCase.class);
        CreateClusterStateUseCase createClusterStateUseCase = Mockito.mock(CreateClusterStateUseCase.class);
        UpdateClusterStateUseCase updateClusterStateUseCase = Mockito.mock(UpdateClusterStateUseCase.class);
        ClusterStateRestMapper restMapper = Mockito.mock(ClusterStateRestMapper.class);

        ClusterStateController controller = new ClusterStateController(
                getClusterStatusUseCase, registerInitialUseCase,
                createClusterStateUseCase, updateClusterStateUseCase, restMapper);
        ReflectionTestUtils.setField(controller, "defaultEnvironment", "dev");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void getReturns200WithEnvelope() throws Exception {
        when(getClusterStatusUseCase.getStatus(any(GetClusterStatusQuery.class)))
                .thenReturn(new ClusterStatusResult(true, 30));

        mockMvc.perform(get("/v1/pass/arqs/cluster-availability-service/get-cluster-status")
                        .header("APP-NAME", "app")
                        .header("PROJECT-NAME", "proj")
                        .header("ENVIRONMENT", "dev"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.method").value("GET"))
                .andExpect(jsonPath("$.data.active").value(true))
                .andExpect(jsonPath("$.data.pollingIntervalSeconds").value(30));
    }

    @Test
    void getMapsNotFoundToAcs004() throws Exception {
        when(getClusterStatusUseCase.getStatus(any(GetClusterStatusQuery.class)))
                .thenThrow(new ClusterStateNotFoundException("no encontrado"));

        mockMvc.perform(get("/v1/pass/arqs/cluster-availability-service/get-cluster-status"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].code").value("ACS-004"));
    }

    @Test
    void getMapsInvalidParametersToAcs001() throws Exception {
        when(getClusterStatusUseCase.getStatus(any(GetClusterStatusQuery.class)))
                .thenThrow(new InvalidParametersException("parametros invalidos"));

        mockMvc.perform(get("/v1/pass/arqs/cluster-availability-service/get-cluster-status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].code").value("ACS-001"));
    }
}
