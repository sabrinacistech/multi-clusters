package com.sabrinacistech.multiclusters.dto;

public record ResponseApiClusterStatus(
    boolean success,
    String message,
    ResponseMetadata metadata,
    ClusterStatusDto data
) {

    public static ResponseApiClusterStatus ok(ResponseMetadata metadata, ClusterStatusDto data) {
        return new ResponseApiClusterStatus(true, "Cluster status retrieved successfully", metadata, data);
    }
}
