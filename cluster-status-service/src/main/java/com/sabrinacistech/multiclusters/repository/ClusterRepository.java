package com.sabrinacistech.multiclusters.repository;

import java.util.*;

import com.sabrinacistech.multiclusters.model.Cluster;

/**
 * Puerto de salida para el acceso al agregado {@link Cluster}.
 *
 * <p><b>Capa:</b> domain.port.</p>
 * <p><b>Responsabilidad:</b> definir las operaciones que la aplicación necesita para
 * leer el estado de un clúster, sin acoplarse a ningún detalle de
 * infraestructura (JPA, SQL, cache, etc.).</p>
 *
 * <p><b>Contrato general:</b>
 * <ul>
 *   <li>Las implementaciones deben ser thread-safe si se comparten entre hilos.</li>
 *   <li>Este puerto no impone la tecnología ni el modelo de persistencia.</li>
 *   <li>Las implementaciones pueden lanzar excepciones de dominio (p.ej. errores de acceso a datos)
 *       como {@code RuntimeException} según corresponda.</li>
 * </ul>
 * </p>
 */
public interface ClusterRepository {
    /**
     * Busca un clúster por su alias lógico.
     *
     * @param alias identificador lógico del clúster (no debe ser {@code null} ni vacío)
     * @return {@code Optional} con el {@link Cluster} si existe; {@code Optional.empty()} en caso contrario
     * @implNote La implementación puede usar caché o lecturas optimizadas,
     * pero no debe alterar la semántica del resultado.
     */
    Optional<Cluster> findByAlias(String alias);


}
