package ar.com.paas.cluster.status.monitor.domain.model;

import lombok.Value;

/**
 * Objetivo de sondeo: identifica una app/clúster y su URL de health.
 */
@Value
public class ClusterTarget {

    String app;
    String cluster;
    String healthUrl;

    public ClusterTarget(String app, String cluster, String healthUrl) {
        this.app = app;
        this.cluster = cluster;
        this.healthUrl = healthUrl;
    }

    /** Nombre lógico del target: el clúster. */
    public String name() {
        return cluster;
    }
}
