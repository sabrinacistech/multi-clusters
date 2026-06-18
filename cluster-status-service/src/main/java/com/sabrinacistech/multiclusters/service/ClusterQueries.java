package com.sabrinacistech.multiclusters.service;

import org.slf4j.*;


import com.sabrinacistech.multiclusters.model.Cluster;
import com.sabrinacistech.multiclusters.repository.ClusterRepository;
import com.sabrinacistech.multiclusters.config.ClusterConfig;
import com.sabrinacistech.multiclusters.enums.*;
import  com.sabrinacistech.multiclusters.exception.*;

import java.util.*;

/**
 * Orquestador de lectura del clúster configurado.
 *
 * <p><b>Capa:</b> application.</p>
 * <p><b>Responsabilidad:</b> resolver el alias desde {@link ClusterConfig} y obtener el clúster
 * mediante {@link ClusterRepository}.</p>
 *
 * <p><b>Variantes:</b>
 * {@link #findConfiguredCluster()} retorna {@code Optional},
 * {@link #requireConfiguredCluster()} lanza {@link ClusterNotFoundException} si no existe.</p>
 */


public class ClusterQueries {
    private final ClusterRepository repository;
    private final ClusterConfig config;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterQueries.class);

    public ClusterQueries(ClusterRepository repository, ClusterConfig config) {
        this.repository = repository;
        this.config = config;
    }

    /**
     * Obtiene el clúster configurado o lanza excepción si no existe.
     *
     * @return el clúster para el alias configurado
     * @throws ClusterNotFoundException si no hay registro para el alias
     */
    public Cluster requireConfiguredCluster() {
        return findConfiguredCluster().orElseThrow(() -> {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(LogKey.QUERY_CONFIG_NOT_FOUND.getValue(), config.getDataCenter(), ClusterEvent.SYNC_EXECUTE.getValue(), ClusterAction.RELOAD_CONFIG.getValue(), ClusterReason.CONFIG_NOT_FOUND.getValue());
            }
            return new ClusterNotFoundException(ClusterExceptionMessage.CLUSTER_NOT_FOUND.getMessage());
        });
    }

    /**
     * Busca el clúster para el alias configurado.
     *
     * @return {@code Optional} con el clúster si existe; vacío en caso contrario
     */
    public Optional<Cluster> findConfiguredCluster() {
        return repository.findByAlias(config.getDataCenter());
    }


}