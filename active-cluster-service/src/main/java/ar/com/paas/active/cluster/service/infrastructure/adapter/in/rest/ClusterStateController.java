package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest;

import ar.com.paas.active.cluster.service.application.port.in.ClusterStateCommand;
import ar.com.paas.active.cluster.service.application.port.in.CreateClusterStateUseCase;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.ClusterStatusResult;
import ar.com.paas.active.cluster.service.application.port.in.GetClusterStatusUseCase.GetClusterStatusQuery;
import ar.com.paas.active.cluster.service.application.port.in.RegisterInitialUseCase;
import ar.com.paas.active.cluster.service.application.port.in.UpdateClusterStateUseCase;
import ar.com.paas.active.cluster.service.domain.model.ClusterState;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ApiResponse;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ClusterStateDTO;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ClusterStatusDTO;
import ar.com.paas.active.cluster.service.infrastructure.mapper.ClusterStateRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST del active-cluster-service (READER).
 * Se implementa como @RestController plano para garantizar compilacion
 * independiente de la interfaz generada por OpenAPI.
 */
@Slf4j
@RestController
@RequestMapping("/v1/pass/arqs/cluster-availability-service")
@Tag(name = "ClusterState", description = "Operaciones de estado de cluster")
public class ClusterStateController {

    private final GetClusterStatusUseCase getClusterStatusUseCase;
    private final RegisterInitialUseCase registerInitialUseCase;
    private final CreateClusterStateUseCase createClusterStateUseCase;
    private final UpdateClusterStateUseCase updateClusterStateUseCase;
    private final ClusterStateRestMapper restMapper;

    @Value("${acs.default-environment:dev}")
    private String defaultEnvironment;

    public ClusterStateController(GetClusterStatusUseCase getClusterStatusUseCase,
                                  RegisterInitialUseCase registerInitialUseCase,
                                  CreateClusterStateUseCase createClusterStateUseCase,
                                  UpdateClusterStateUseCase updateClusterStateUseCase,
                                  ClusterStateRestMapper restMapper) {
        this.getClusterStatusUseCase = getClusterStatusUseCase;
        this.registerInitialUseCase = registerInitialUseCase;
        this.createClusterStateUseCase = createClusterStateUseCase;
        this.updateClusterStateUseCase = updateClusterStateUseCase;
        this.restMapper = restMapper;
    }

    @Operation(summary = "Obtiene el estado del cluster para la aplicacion consultante")
    @GetMapping("/get-cluster-status")
    public ResponseEntity<ApiResponse<ClusterStatusDTO>> getClusterStatus(
            @RequestHeader(value = "APP-NAME", required = false, defaultValue = "Unknown-App-Name")
            String appName,
            @RequestHeader(value = "PROJECT-NAME", required = false, defaultValue = "Unknown-Project-Name")
            String projectName,
            @RequestHeader(value = "CLIENT-CLUSTER-ACTIVE", required = false, defaultValue = "Unknown-Reported-Cluster")
            String reportedCluster,
            @RequestHeader(value = "ENVIRONMENT", required = false)
            String environment) {

        String env = (environment == null || environment.isBlank()) ? defaultEnvironment : environment;
        GetClusterStatusQuery query = new GetClusterStatusQuery(appName, projectName, env, reportedCluster);
        ClusterStatusResult result = getClusterStatusUseCase.getStatus(query);

        ClusterStatusDTO data = new ClusterStatusDTO(result.active(), result.pollingIntervalSeconds());
        return ResponseEntity.ok(ApiResponse.ok("GET", "getClusterStatus", data));
    }

    @Operation(summary = "Registro inicial del estado de cluster")
    @PostMapping("/inicial")
    public ResponseEntity<ApiResponse<ClusterStateDTO>> registerInitial(
            @RequestBody ClusterStateDTO body) {
        ClusterState state = registerInitialUseCase.registerInitial(toCommand(body));
        return ResponseEntity.ok(ApiResponse.ok("POST", "registerInitial", restMapper.toDto(state)));
    }

    @Operation(summary = "Crea un estado de cluster")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ClusterStateDTO>> createClusterState(
            @RequestBody ClusterStateDTO body) {
        ClusterState state = createClusterStateUseCase.create(toCommand(body));
        return ResponseEntity.ok(ApiResponse.ok("POST", "createClusterState", restMapper.toDto(state)));
    }

    @Operation(summary = "Actualiza un estado de cluster")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ClusterStateDTO>> updateClusterState(
            @RequestBody ClusterStateDTO body) {
        ClusterState state = updateClusterStateUseCase.update(toCommand(body));
        return ResponseEntity.ok(ApiResponse.ok("PUT", "updateClusterState", restMapper.toDto(state)));
    }

    private ClusterStateCommand toCommand(ClusterStateDTO dto) {
        return new ClusterStateCommand(
                dto.id(),
                dto.application(),
                dto.project(),
                dto.environment(),
                dto.activeCluster(),
                dto.active(),
                dto.pollingIntervalSeconds(),
                dto.updatedBy(),
                dto.reason(),
                dto.metadata());
    }
}
