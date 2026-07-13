package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest;

import ar.com.paas.cluster.status.monitor.application.port.in.ManageAliasStatusUseCase;
import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.AliasStatusDTO;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.AliasStatusInput;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ApiResponse;
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
 * Controlador ABM de estados de alias.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AbmAliasStatusController {

    private final ManageAliasStatusUseCase useCase;

    @GetMapping("/alias-status")
    public ResponseEntity<ApiResponse<List<AliasStatusDTO>>> list() {
        List<AliasStatusDTO> data = useCase.list().stream().map(this::toDto).toList();
        return ResponseEntity.ok(ApiResponse.ok("GET", "list-alias-status", data));
    }

    @PostMapping("/alias-status")
    public ResponseEntity<ApiResponse<AliasStatusDTO>> create(@Valid @RequestBody AliasStatusInput input) {
        AliasStatus created = useCase.create(toDomain(input));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("POST", "create-alias-status", toDto(created)));
    }

    @PutMapping("/alias-status/{alias}")
    public ResponseEntity<ApiResponse<AliasStatusDTO>> update(@PathVariable String alias,
                                                              @Valid @RequestBody AliasStatusInput input) {
        AliasStatus updated = useCase.update(alias, toDomain(input));
        return ResponseEntity.ok(ApiResponse.ok("PUT", "update-alias-status", toDto(updated)));
    }

    @DeleteMapping("/alias-status/{alias}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String alias) {
        useCase.delete(alias);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.ok("DELETE", "delete-alias-status", null));
    }

    private AliasStatus toDomain(AliasStatusInput in) {
        return AliasStatus.builder()
                .alias(in.alias())
                .app(in.app())
                .cluster(in.cluster())
                .target(in.target())
                .isActive(in.isActive())
                .build();
    }

    private AliasStatusDTO toDto(AliasStatus s) {
        return new AliasStatusDTO(s.getId(), s.getAlias(), s.getApp(), s.getCluster(),
                s.getTarget(), s.getIsActive(), s.getUpdatedAt());
    }
}
