package ar.com.paas.active.cluster.service.domain.exception;

/**
 * Se lanza cuando no se encuentra un estado de cluster (ACS-004).
 */
public class ClusterStateNotFoundException extends DomainException {

    public ClusterStateNotFoundException(String message) {
        super(ErrorCode.CLUSTER_STATE_NOT_FOUND.code(), message);
    }
}
