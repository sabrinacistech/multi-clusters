package ar.com.paas.cluster.status.monitor.domain.model;

import lombok.Value;

import java.time.Duration;

/**
 * Resultado de un sondeo de salud contra un clúster.
 */
@Value
public class HealthResult {

    boolean up;
    Duration latency;
    String reason;

    /** Resultado sano sin latencia registrada. */
    public static HealthResult up() {
        return new HealthResult(true, Duration.ZERO, null);
    }

    /** Resultado sano con latencia registrada. */
    public static HealthResult up(Duration latency) {
        return new HealthResult(true, latency == null ? Duration.ZERO : latency, null);
    }

    /** Resultado no sano con motivo. */
    public static HealthResult down(String reason) {
        return new HealthResult(false, Duration.ZERO, reason);
    }
}
