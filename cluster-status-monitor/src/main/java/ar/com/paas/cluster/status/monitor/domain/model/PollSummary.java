package ar.com.paas.cluster.status.monitor.domain.model;

/**
 * Resumen de un ciclo de sondeo: cantidad de clústeres procesados con éxito y con fallo.
 */
public class PollSummary {

    private final int succeeded;
    private final int failed;

    public PollSummary(int ok, int failed) {
        this.succeeded = ok;
        this.failed = failed;
    }

    public int getSucceeded() {
        return succeeded;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return succeeded + failed;
    }

    @Override
    public String toString() {
        return "PollSummary{succeeded=" + succeeded + ", failed=" + failed + '}';
    }
}
