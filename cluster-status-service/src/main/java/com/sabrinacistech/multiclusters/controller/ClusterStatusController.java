package com.sabrinacistech.multiclusters.controller;

import com.sabrinacistech.multiclusters.openapi.model.ClusterStatusIsActiveDTO;
import com.sabrinacistech.multiclusters.openapi.model.MetaData;
import com.sabrinacistech.multiclusters.openapi.model.ResponseApiClusterStatus;
import com.sabrinacistech.multiclusters.service.ClusterStatusStore;
import com.sabrinacistech.multiclusters.util.LogSanitizer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterStatusController {

    private final ClusterStatusStore clusterStatusStore;

    public ClusterStatusController(ClusterStatusStore clusterStatusStore) {
        this.clusterStatusStore = clusterStatusStore;
    }

    /**
     * Obtiene el estado actual del clúster desde la base de datos MongoDB.
     *
     * <p>Responde con el contrato {@code ResponseApiClusterStatus} definido en
     * {@code src/main/resources/openapi/cluster-status.yaml}, que incluye los bloques
     * {@code meta}, {@code data} y {@code errors}.</p>
     *
     * @param request request HTTP usado para construir el bloque {@code meta}
     * @return 200 OK con el estado del clúster
     */
    @GetMapping("/get-cluster-status")
    public ResponseEntity<ResponseApiClusterStatus> getClusterStatus(HttpServletRequest request) {
        ClusterStatusIsActiveDTO status = clusterStatusStore.getCurrentStatus();
        MetaData meta = new MetaData()
            .method(request.getMethod())
            .operation(LogSanitizer.sanitizeForLog(request.getRequestURI()));
        ResponseApiClusterStatus response = new ResponseApiClusterStatus()
            .meta(meta)
            .data(status);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
