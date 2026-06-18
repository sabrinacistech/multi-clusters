package com.sabrinacistech.multiclusters.service;

/**
 * Eventos principales del ciclo de vida del monitoreo.
 */
public enum ClusterEvent {
    SYNC_EXECUTE("cluster_sync_execute"), STATE_CHANGE("cluster_state_change"), HOSTNAME_CHECK("hostname_verification"), FALLBACK_ACTIVATED("resilience_fallback_activated"),
    STATUS_QUERY("cluster_status_query"),
    REFRESH_SCHEDULER("scheduler_config_refresh"), DATABASE_SAVE("database_save_operation");


    private final String value;

    ClusterEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

