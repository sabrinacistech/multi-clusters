package com.sabrinacistech.multiclusters.controller;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import com.sabrinacistech.multiclusters.dto.ClusterStatusDto;
import com.sabrinacistech.multiclusters.dto.ResponseApiClusterStatus;
import com.sabrinacistech.multiclusters.dto.ResponseMetadata;
import com.sabrinacistech.multiclusters.service.ClusterStatusStore;
import com.sabrinacistech.multiclusters.util.LogSanitizer;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterStatusController {

    private final ClusterConfigProperties configProps;
    private final ClusterStatusStore clusterStatusStore;

    public ClusterStatusController(
        ClusterConfigProperties configProps,
        ClusterStatusStore clusterStatusStore
    ) {
        this.configProps = configProps;
        this.clusterStatusStore = clusterStatusStore;
    }

    /**
     * Obtiene el estado actual del clúster desde la base de datos MongoDB.
     *
     * <p>Responde con un payload estándar ({@code ResponseApi}) que incluye metadatos
     * y un DTO con {@code active} y {@code pollingIntervalSeconds}.</p>
     *
     * @param appName     nombre de la aplicación consumidora (header opcional)
     * @param projectName nombre del proyecto consumidor (header opcional)
     * @param clientClusterActive valor del header "CLIENT-CLUSTER-ACTIVE" reportado por el cliente (header opcional)
     * @param request     request HTTP para construir metadata de respuesta
     * @return 200 OK con el estado del clúster
     */
    @ApiOperation(value = "cluster status result", response = String.class, produces = "application/json")
    @GetMapping("/get-cluster-status")
    public ResponseEntity<ResponseApiClusterStatus> getClusterStatus(
        @RequestHeader(value = "APP-NAME", required = false, defaultValue = "Unknown-App-Name") String appName,
        @RequestHeader(value = "PROJECT-NAME", required = false, defaultValue = "Unknown-Project-Name") String projectName,
        @RequestHeader(value = "CLIENT-CLUSTER-ACTIVE", required = false, defaultValue = "Unknown-Reported-Cluster") String clientClusterActive,
        HttpServletRequest request
    ) {
        final String cluster = LogSanitizer.sanitizeForLog(configProps.getDataCenter());
        ClusterStatusDto status = clusterStatusStore.getCurrentStatus();
        ResponseMetadata metadata = ResponseMetadata.from(
            request,
            appName,
            projectName,
            clientClusterActive,
            cluster,
            OffsetDateTime.now(ZoneOffset.UTC)
        );
        ResponseApiClusterStatus response = ResponseApiClusterStatus.ok(metadata, status);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
