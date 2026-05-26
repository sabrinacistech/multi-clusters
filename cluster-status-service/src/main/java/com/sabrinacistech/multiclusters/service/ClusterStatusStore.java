package com.sabrinacistech.multiclusters.service;

import com.sabrinacistech.multiclusters.config.ClusterConfigProperties;
import com.sabrinacistech.multiclusters.dto.ClusterStatusDto;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class ClusterStatusStore {

    private final AtomicReference<ClusterStatusDto> currentStatus;

    public ClusterStatusStore(ClusterConfigProperties configProps) {
        this.currentStatus = new AtomicReference<>(
            new ClusterStatusDto(configProps.isActive(), configProps.getPollingIntervalSeconds())
        );
    }

    public ClusterStatusDto getCurrentStatus() {
        return currentStatus.get();
    }

    public void updateStatus(boolean active, int pollingIntervalSeconds) {
        currentStatus.set(new ClusterStatusDto(active, pollingIntervalSeconds));
    }
}
