package com.sabrinacistech.multiclusters.enums;


public enum ClusterExceptionMessage {

    INITIAL_STATE_UNKNOWN("The state of the initial cluster could not be determined"),

    INVALID_PARAMETERS("Command URL and alias cannot be null or empty"),
    CLUSTER_NOT_FOUND("No configuration found for the specified cluster alias"),

    STATUS_UNAVAILABLE("Cluster status is currently unavailable in the memory store"),
    DATABASE_ACCESS_ERROR("An error occurred while accessing the cluster data persistence"),

    // Networking & Infrastructure Errors
    RESOLUTION_FAILED("Failed to resolve hostname for the provided URL"),
    SYNC_EXECUTION_FAILED("State synchronization execution failed"),

    // Dynamic Configuration Errors
    SCHEDULER_CONFIG_ERROR("Error calculating or applying the dynamic scheduler delay"),
    CONFIG_REFRESH_FAILED("Could not refresh cluster configuration from projection"),
    DATABASE_ERROR("Database access error");

    private final String message;

    ClusterExceptionMessage(String message) {
        this.message = message;
    }

    /**
     * @return The technical message in English.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}