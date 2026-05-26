package com.sabrinacistech.multiclusters.service;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import com.sabrinacistech.multiclusters.dto.ClusterStatusDto;
import com.sabrinacistech.multiclusters.repository.ClusterStatusCacheRepository;
import org.springframework.stereotype.Component;

@Component
public class ClusterStatusStore {

    private final ClusterConfigProperties configProps;
    private final ClusterStatusCacheRepository repository;

    public ClusterStatusStore(
        ClusterConfigProperties configProps,
        ClusterStatusCacheRepository repository
    ) {
        this.configProps = configProps;
        this.repository = repository;
    }

    public ClusterStatusDto getCurrentStatus() {
        return repository.findFirstByDataCenterOrderByUpdatedAtDesc(configProps.getDataCenter())
            .map(cache -> new ClusterStatusDto(cache.isActive(), cache.getPollingIntervalSeconds()))
            .orElseGet(() -> new ClusterStatusDto(configProps.isActive(), configProps.getPollingIntervalSeconds()));
    }
}
