package com.sabrinacistech.multiclusters.service;

/**
 * Acciones técnicas ejecutadas por el servicio.
 */
public enum ClusterAction {
    CLEAR_STORE("clear_in_memory_store"),
    UPDATE_REPO("update_repository"),
    RESOLVE_HOST("resolve_hostname"),
    USING_FALLBACK("using_graceful_degradation"),
    RELOAD_CONFIG("reload_configuration"),
    CALCULATE_DELAY("calculate_dynamic_delay"),
    FALLBACK("resilience_fallback_activated"),
    STARTUP_INITIALIZATION("startup_initialization"),
    PERSIST_ENTITY("persist_cluster_entity"),
    READ_STORE("read_in_memory_store");;

    private final String value;

    ClusterAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}