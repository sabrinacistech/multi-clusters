package ar.com.paas.cluster.status.monitor.application.port.in;

import ar.com.paas.cluster.status.monitor.domain.model.PollSummary;

/**
 * Caso de uso: sondear el estado de todos los clústeres registrados.
 */
public interface PollClusterStatusUseCase {

    /**
     * Sondea todos los clústeres registrados y persiste/refresca su estado.
     *
     * @return resumen del ciclo (éxitos/fallos).
     */
    PollSummary pollAll();
}
