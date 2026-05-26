package com.sabrinacistech.multiclusters.service;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import com.sabrinacistech.multiclusters.dto.ClusterStatusDto;
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

    public ClusterStatusDto getCurrentStatus() {
        return repository.findFirstByDataCenterOrderByUpdatedAtDesc(configProps.getDataCenter())
            .map(status -> new ClusterStatusDto(status.isActive(), status.getPollingIntervalSeconds()))
            .orElseGet(() -> new ClusterStatusDto(configProps.isActive(), configProps.getPollingIntervalSeconds()));
    }
}
