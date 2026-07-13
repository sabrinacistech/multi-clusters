package ar.com.paas.cluster.status.monitor.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Propiedades de configuración del servicio (prefijo {@code csm}).
 */
@Data
@ConfigurationProperties(prefix = "csm")
public class CsmProperties {

    private Scheduler scheduler = new Scheduler();
    private Probe probe = new Probe();
    private Cache cache = new Cache();
    private List<ClusterDef> clusters = new ArrayList<>();
    /** Ventana de gracia (segundos) para respetar cambios manuales. */
    private long manualGraceSeconds = 300;

    @Data
    public static class Scheduler {
        private boolean enabled = true;
        private long fixedDelay = 30000;
        private long initialDelay = 10000;
        private String cron;
    }

    @Data
    public static class Probe {
        private int timeoutMs = 3000;
        private int maxRetries = 2;
        private long retryBackoffMs = 500;
    }

    @Data
    public static class Cache {
        private long ttlSeconds = 30;
    }

    @Data
    public static class ClusterDef {
        private String app;
        private String cluster;
        private String healthUrl;
    }
}
