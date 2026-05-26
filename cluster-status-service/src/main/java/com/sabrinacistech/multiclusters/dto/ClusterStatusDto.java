package com.sabrinacistech.multiclusters.dto;

public record ClusterStatusDto(
    boolean active,
    int pollingIntervalSeconds
) {
}
