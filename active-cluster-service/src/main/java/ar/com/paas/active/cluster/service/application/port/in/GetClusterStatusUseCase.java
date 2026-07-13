package ar.com.paas.active.cluster.service.application.port.in;

/**
 * Puerto de entrada para consultar el estado de un cluster.
 */
public interface GetClusterStatusUseCase {

    ClusterStatusResult getStatus(GetClusterStatusQuery query);

    /**
     * Consulta de estado de cluster.
     */
    record GetClusterStatusQuery(
            String application,
            String project,
            String environment,
            String reportedCluster) {
    }

    /**
     * Resultado de la consulta de estado.
     */
    record ClusterStatusResult(
            boolean active,
            int pollingIntervalSeconds) {
    }
}
