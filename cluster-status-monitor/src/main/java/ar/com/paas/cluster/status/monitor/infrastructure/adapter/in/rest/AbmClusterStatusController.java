package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest;

import ar.com.paas.cluster.status.monitor.application.port.in.ManageClusterStatusUseCase;
import ar.com.paas.cluster.status.monitor.domain.model.ClusterStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ApiResponse;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ClusterStatusDTO;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ClusterStatusInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador ABM de estados de clúster. Devuelve todo dentro del sobre PaaS.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AbmClusterStatusController {

    private final ManageClusterStatusUseCase useCase;

    @GetMapping("/cluster-status")
    public ResponseEntity<ApiResponse<List<ClusterStatusDTO>>> list() {
        List<ClusterStatusDTO> data = useCase.list().stream().map(this::toDto).toList();
        return ResponseEntity.ok(ApiResponse.ok("GET", "list-cluster-status", data));
    }

    @GetMapping("/cluster-status/{id}")
    public ResponseEntity<ApiResponse<ClusterStatusDTO>> get(@PathVariable String id) {
        ClusterStatusDTO data = toDto(useCase.get(id));
        return ResponseEntity.ok(ApiResponse.ok("GET", "get-cluster-status", data));
    }

    @PostMapping("/cluster-status")
    public ResponseEntity<ApiResponse<ClusterStatusDTO>> create(@Valid @RequestBody ClusterStatusInput input) {
        ClusterStatus created = useCase.create(toDomain(null, input));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("POST", "create-cluster-status", toDto(created)));
    }

    @PutMapping("/cluster-status/{id}")
    public ResponseEntity<ApiResponse<ClusterStatusDTO>> update(@PathVariable String id,
                                                                @Valid @RequestBody ClusterStatusInput input) {
        ClusterStatus updated = useCase.update(id, toDomain(id, input));
        return ResponseEntity.ok(ApiResponse.ok("PUT", "update-cluster-status", toDto(updated)));
    }

    @DeleteMapping("/cluster-status/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        useCase.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.ok("DELETE", "delete-cluster-status", null));
    }

    private ClusterStatus toDomain(String id, ClusterStatusInput in) {
        return ClusterStatus.builder()
                .id(id)
                .codigo(in.codigo())
                .app(in.app())
                .cluster(in.cluster())
                .isActive(in.isActive())
                .updatedBy(in.updatedBy() != null ? in.updatedBy() : ClusterStatus.UPDATED_BY_MANUAL)
                .build();
    }

    private ClusterStatusDTO toDto(ClusterStatus s) {
        return new ClusterStatusDTO(
                s.getId(), s.getCodigo(), s.getApp(), s.getCluster(), s.getIsActive(),
                s.getUpdatedBy(), s.getUpdatedAt(), s.getLastManualAt(), s.getVersion());
    }
}
