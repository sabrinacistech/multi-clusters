package com.sabrinacistech.multiclusters.service;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import com.sabrinacistech.multiclusters.openapi.model.ClusterStatusIsActiveDTO;
import com.sabrinacistech.multiclusters.repository.ClusterStatusRepository;
import org.springframework.stereotype.Component;

@Component
public class ClusterStatusStore {

    private final ClusterConfigProperties configProps;
    private final ClusterStatusRepository repository;

    public ClusterStatusStore(
        ClusterConfigProperties configProps,
        ClusterStatusRepository repository
    ) {
        this.configProps = configProps;
        this.repository = repository;
    }

    public ClusterStatusIsActiveDTO getCurrentStatus() {
        return repository.findFirstByDataCenterOrderByUpdatedAtDesc(configProps.getDataCenter())
            .map(status -> new ClusterStatusIsActiveDTO()
                .active(status.isActive())
                .pollingIntervalSeconds(status.getPollingIntervalSeconds()))
            .orElseGet(() -> new ClusterStatusIsActiveDTO()
                .active(configProps.isActive())
                .pollingIntervalSeconds(configProps.getPollingIntervalSeconds()));
    }
}
